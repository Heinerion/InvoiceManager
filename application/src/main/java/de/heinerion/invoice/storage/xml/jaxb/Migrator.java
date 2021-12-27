package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.repositories.AddressRepository;
import de.heinerion.betriebe.repositories.CompanyRepository;
import de.heinerion.betriebe.repositories.TemplateRepository;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.storage.PathTools;
import de.heinerion.invoice.storage.loading.FileHandler;
import de.heinerion.invoice.storage.xml.jaxb.migration.AddressLoader;
import de.heinerion.invoice.storage.xml.jaxb.migration.CompanyLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static de.heinerion.betriebe.services.ConfigurationService.PropertyKey.*;

/**
 * This class is to be used as stand alone tool for migrating one folder layout to another.
 * <p>It creates xml files containing addresses, companies and templates along the way</p>
 * <p>
 * <strong>Initial structure</strong> (with translation properties in square brackets)
 * <pre>
 *   [application base]
 *   |-- [invoice]
 *   |   `-- &lt;company>
 *   |       `-- &lt;invoice>.pdf
 *   |
 *   |-- [letter]
 *   |   `-- &lt;letter>.pdf
 *   |
 *   `-- [system]
 *       |-- [invoice]
 *       |   `-- &lt;company>
 *       |       `-- &lt;invoice>.tex
 *       |
 *       `-- [letter]
 *           `-- &lt;letter>.tex
 * </pre>
 * <strong>Migrated structure</strong> (with translation properties in square brackets)
 * <pre>
 *   [application base]
 *   |-- &lt;company>
 *   |   |-- [invoice]
 *   |   |   |-- [sources]
 *   |   |   |   `-- &lt;invoice>.tex
 *   |   |   |
 *   |   |   `-- &lt;invoice>.pdf
 *   |   |
 *   |   |-- [letter]
 *   |   |   |-- [sources]
 *   |   |   |   `-- &lt;letter>.tex
 *   |   |   |
 *   |   |   `-- &lt;letter>.pdf
 *   |   |
 *   |   |-- addresses.xml
 *   |   `-- templates.xml
 *   |
 *   `-- companies.xml
 * </pre>
 * </p>
 */
@Flogger
@Service
@RequiredArgsConstructor
public class Migrator {
  private final PathUtilNG pathUtil;
  private final AddressRepository addressRepository;
  private final CompanyRepository companyRepository;
  private final TemplateRepository templateRepository;

  public static void main(String... args) {
    Migrator bean = ConfigurationService.getBean(Migrator.class);
    bean.migrateCompanies();
    ConfigurationService.exitApplication();
  }

  @PostConstruct
  public void post() {
    migrateCompanies();
  }

  public void migrateCompanies() {
    // transfer companies to new approach
    File companyDir = new File(PathTools.getPath(Company.class, pathUtil));
    log.atFine().log("read %s", companyDir);
    CompanyLoader legacyLoader = new CompanyLoader(companyDir);
    legacyLoader.init();
    List<Company> availableCompanies = legacyLoader.load(addressRepository).stream()
        .map(Company.class::cast)
        .sorted(Company::compareTo)
        .map(company -> {
          if (company.getId() != null) {
            return company;
          }
          return company.setId(UUID.randomUUID());
        })
        .toList();
    log.atInfo().log("Companies found: %s", availableCompanies);

    Set<Company> persistent = new HashSet<>(companyRepository.findAll());
    List<Company> newCompanies = availableCompanies.stream()
        .filter(c -> persistent.stream().noneMatch(alt -> alt.getDescriptiveName().equals(c.getDescriptiveName())))
        .toList();
    persistent.addAll(newCompanies);
    companyRepository.saveAll(persistent);
    newCompanies.forEach(company -> migrateCompanyInfo(pathUtil, company));

    migrateAddresses();
  }

  private void migrateAddresses() {
    File addressDir = new File(PathTools.getPath(Address.class, pathUtil));
    AddressLoader legacyAddressLoader = new AddressLoader(addressDir);
    legacyAddressLoader.init();
    List<Address> addresses = legacyAddressLoader.load(addressRepository).stream()
        .map(Address.class::cast)
        .toList();
    addressRepository.saveAll(addresses);
  }

  private static void print(String message) {
    System.out.println(message);
  }

  private void migrateCompanyInfo(PathUtilNG pathUtil, Company company) {
    log.atInfo().log("Migrate %s", company);
    File companyDir = createDir(pathUtil.getBaseDir(), company.getDescriptiveName());
    logCreation(companyDir);

    transferTemplatesAndAddresses(company);

    copyLettersAndInvoices(pathUtil, company, companyDir);
  }

  private static void logCreation(File createdFile) {
    log.atInfo().log("created %s", createdFile);
  }

  private void transferTemplatesAndAddresses(Company company) {
    Collection<InvoiceTemplate> oldTemplates = templateRepository.findAll();

    List<InvoiceTemplate> templates = FileHandler
        .load(new InvoiceTemplate(), pathUtil.getTemplateFileName(company.getDescriptiveName()))
        .stream()
        .filter(template -> template.getInhalt() == null)
        .filter(template -> oldTemplates.stream().noneMatch(t -> t.getName().equals(template.getName())))
        .map(template -> {
          template.setInhalt(new String[0][0]);
          if (template.getCompanyId() == null) {
            template.setCompanyId(company.getId());
          }
          return template;
        })
        .toList();

    templateRepository.saveAll(templates);
  }

  private static void copyLettersAndInvoices(PathUtilNG pathUtil, Company company, File companyDir) {
    File home = new File(pathUtil.getBaseDir());

    log.atInfo().log("Move letters");
    String letterDirName = ConfigurationService.get(FOLDER_LETTERS);
    File oldLetterDir = new File(home, letterDirName);
    File newLetterDir = createDir(companyDir, letterDirName);

    copyFiles(company, oldLetterDir, newLetterDir);

    log.atInfo().log("Move letter sources");
    String systemDirName = ConfigurationService.get(FOLDER_SYSTEM);
    File systemDir = new File(home, systemDirName);
    File oldLetterSrcDir = new File(systemDir, letterDirName);

    String srcDirName = ConfigurationService.get(FOLDER_SOURCES);
    File newLetterSrcDir = createDir(newLetterDir, srcDirName);

    copyFiles(company, oldLetterSrcDir, newLetterSrcDir);

    log.atInfo().log("Move invoices");
    String invoiceDirName = ConfigurationService.get(FOLDER_INVOICES);
    String invoiceIdentifier = invoiceDirName + File.separator + company.getDescriptiveName();
    File oldInvoiceDir = new File(home, invoiceIdentifier);
    File newInvoiceDir = createDir(companyDir, invoiceDirName);
    if (oldInvoiceDir.exists()) {
      copy(oldInvoiceDir, newInvoiceDir);
    }

    log.atInfo().log("Move invoice sources");
    File oldInvoiceSrcDir = new File(systemDir, invoiceIdentifier);
    File newInvoiceSrcDir = createDir(newInvoiceDir, srcDirName);

    copyFiles(company, oldInvoiceSrcDir, newInvoiceSrcDir);
  }

  private static void copyFiles(Company company, File oldLetterDir, File newLetterDir) {
    File[] letterPDFs = oldLetterDir.listFiles();
    if (letterPDFs != null) {
      Predicate<File> fileBelongsToCompany = pdf -> isOfCompany(company, pdf);
      Consumer<File> copyFileToNewLocation = pdf -> copy(pdf, new File(newLetterDir, pdf.getName()));

      Arrays.stream(letterPDFs)
          .filter(fileBelongsToCompany)
          .forEach(copyFileToNewLocation);
    }
  }

  private static boolean isOfCompany(Company company, File file) {
    String author = company.getOfficialName();
    String fileName = file.getName();

    return fileName.endsWith("pdf") && pdfHasAuthor(file, author)
        || fileName.endsWith("tex") && texHasAuthor(file, author);
  }

  private static boolean pdfHasAuthor(File file, String author) {
    try (PDDocument pdf = PDDocument.load(file)) {
      PDDocumentInformation info = pdf.getDocumentInformation();

      return author.equals(info.getAuthor());
    } catch (IOException e) {
      throw new MigrationException(e);
    }
  }

  private static boolean texHasAuthor(File file, String author) {
    boolean authorFound = false;

    try (Scanner scanner = new Scanner(file)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.contains("pdfauthor={" + author + "}")) {
          authorFound = true;
          break;
        }
      }
    } catch (FileNotFoundException e) {
      throw new MigrationException(e);
    }

    return authorFound;
  }

  private static void copy(File src, File dest) {
    try {
      print(String.format("copy %s%n  to %s", src, dest));
      FileSystemUtils.copyRecursively(src, dest);
    } catch (IOException e) {
      throw new MigrationException(e);
    }
  }

  private static File createDir(String parentPath, String name) {
    return createDir(new File(parentPath), name);
  }

  private static File createDir(File parent, String name) {
    File companyFolder = new File(parent, name);
    if ((companyFolder.exists() && companyFolder.isDirectory()) || companyFolder.mkdir()) {
      return companyFolder;
    }
    throw new MigrationException("the folder " + companyFolder + " could not be created!");
  }

  private static class MigrationException extends RuntimeException {
    MigrationException(Throwable e) {
      super("Exception whilst migrating", e);
    }

    MigrationException(String reason) {
      super(reason);
    }
  }
}
