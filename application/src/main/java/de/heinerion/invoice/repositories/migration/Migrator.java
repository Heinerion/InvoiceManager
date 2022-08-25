package de.heinerion.invoice.repositories.migration;

import de.heinerion.invoice.models.Address;
import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.models.InvoiceTemplate;
import de.heinerion.invoice.repositories.XmlPersistence;
import de.heinerion.invoice.repositories.address.AddressRepository;
import de.heinerion.invoice.repositories.company.CompanyRepository;
import de.heinerion.invoice.repositories.template.TemplateRepository;
import de.heinerion.invoice.services.ConfigurationService;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static de.heinerion.invoice.services.ConfigurationService.PropertyKey.*;

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
  private final XmlPersistence persistence;

  public static void main(String... args) {
    // TODO repair...
    // TODO define what to repair...
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
    Path companyDir = PathTools.getPath(Company.class, pathUtil);
    log.atFine().log("read %s", companyDir);
    CompanyLoader legacyLoader = new CompanyLoader(companyDir);
    legacyLoader.init();
    List<Company> legacyCompanies = legacyLoader.load().stream()
        .map(Company.class::cast)
        .sorted(Company::compareTo)
        .map(company -> {
          if (company.getId() != null) {
            return company;
          }
          return company.setId(UUID.randomUUID());
        })
        .toList();
    log.atInfo().log("Companies found: %s", legacyCompanies);

    Set<Company> existingCompanies = new HashSet<>(companyRepository.findAll());
    List<Company> newCompanies = legacyCompanies.stream()
        .filter(c -> existingCompanies.stream().noneMatch(alt -> alt.getDescriptiveName().equals(c.getDescriptiveName())))
        .toList();
    existingCompanies.addAll(newCompanies);
    companyRepository.saveAll(existingCompanies);
    newCompanies.forEach(company -> migrateCompanyInfo(pathUtil, company));

    migrateAddresses();
  }

  private void migrateAddresses() {
    Path addressDir = PathTools.getPath(Address.class, pathUtil);
    AddressLoader legacyAddressLoader = new AddressLoader(addressDir);
    legacyAddressLoader.init();
    List<Address> addresses = legacyAddressLoader.load().stream()
        .map(Address.class::cast)
        .toList();
    addressRepository.saveAll(addresses);
  }

  private void migrateCompanyInfo(PathUtilNG pathUtil, Company company) {
    log.atInfo().log("Migrate %s", company);
    Path companyDir = createDir(pathUtil.getBasePath(), company.getDescriptiveName());
    logCreation(companyDir);

    transferTemplatesAndAddresses(company);

    copyLettersAndInvoices(pathUtil, company, companyDir);
  }

  private static void logCreation(Path createdFile) {
    log.atInfo().log("created %s", createdFile);
  }

  private void transferTemplatesAndAddresses(Company company) {
    Collection<InvoiceTemplate> oldTemplates = templateRepository.findAll();

    log.atFine().log("read sav files");
    List<InvoiceTemplate> savTemplates = FileHandler
        .load(new InvoiceTemplate(), pathUtil.getTemplateFilePath(company.getDescriptiveName()))
        .stream()
        .filter(template -> template.getInhalt() != null)
        .toList();
    log.atFine().log("%d sav templates found", savTemplates.size());

    log.atFine().log("read xml files");
    List<InvoiceTemplate> xmlTemplates = persistence.readTemplates(generateTemplateXmlFile(company))
        .stream()
        .toList();
    log.atFine().log("%d xml templates found", xmlTemplates.size());

    Set<InvoiceTemplate> templates = new HashSet<>();
    templates.addAll(savTemplates);
    templates.addAll(xmlTemplates);

    templateRepository.saveAll(templates.stream()
        .filter(template -> oldTemplates.stream().noneMatch(t -> t.getName().equals(template.getName())))
        .map(template -> {
          if (template.getCompanyId() == null) {
            template.setCompanyId(company.getId());
          }
          return template;
        })
        .toList()
    );
  }

  private Path generateTemplateXmlFile(Company company) {
    return pathUtil.getBasePath()
        .resolve(company.getDescriptiveName())
        .resolve("templates.xml");
  }

  private static void copyLettersAndInvoices(PathUtilNG pathUtil, Company company, Path companyDir) {
    Path home = pathUtil.getBasePath();

    log.atInfo().log("Move letters");
    String letterDirName = ConfigurationService.get(FOLDER_LETTERS);
    Path oldLetterDir = home.resolve(letterDirName);
    Path newLetterDir = createDir(companyDir, letterDirName);

    copyFiles(company, oldLetterDir, newLetterDir);

    log.atInfo().log("Move letter sources");
    Path oldLetterSrcDir = pathUtil.getSystemPath().resolve(letterDirName);

    String srcDirName = ConfigurationService.get(FOLDER_SOURCES);
    Path newLetterSrcDir = createDir(newLetterDir, srcDirName);

    copyFiles(company, oldLetterSrcDir, newLetterSrcDir);

    log.atInfo().log("Move invoices");
    String invoiceDirName = ConfigurationService.get(FOLDER_INVOICES);
    String invoiceIdentifier = invoiceDirName + File.separator + company.getDescriptiveName();
    Path oldInvoiceDir = home.resolve(invoiceIdentifier);
    Path newInvoiceDir = createDir(companyDir, invoiceDirName);
    if (Files.exists(oldInvoiceDir)) {
      copy(oldInvoiceDir, newInvoiceDir);
    }

    log.atInfo().log("Move invoice sources");
    Path oldInvoiceSrcDir = pathUtil.getSystemPath().resolve(invoiceIdentifier);
    Path newInvoiceSrcDir = createDir(newInvoiceDir, srcDirName);

    copyFiles(company, oldInvoiceSrcDir, newInvoiceSrcDir);
  }

  private static void copyFiles(Company company, Path oldLetterDir, Path newLetterDir) {
    Predicate<Path> fileBelongsToCompany = pdf -> isOfCompany(company, pdf);
    Consumer<Path> copyFileToNewLocation = pdf -> copy(pdf, newLetterDir.resolve(pdf.getFileName()));

    try (Stream<Path> files = Files.list(oldLetterDir);) {
      files
          .filter(fileBelongsToCompany)
          .forEach(copyFileToNewLocation);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private static boolean isOfCompany(Company company, Path file) {
    String author = company.getOfficialName();

    return file.endsWith("pdf") && pdfHasAuthor(file, author)
        || file.endsWith("tex") && texHasAuthor(file, author);
  }

  private static boolean pdfHasAuthor(Path path, String author) {
    try (PDDocument pdf = PDDocument.load(path.toFile())) {
      PDDocumentInformation info = pdf.getDocumentInformation();

      return author.equals(info.getAuthor());
    } catch (IOException e) {
      throw new MigrationException(e);
    }
  }

  private static boolean texHasAuthor(Path path, String author) {
    try (Stream<String> lines = Files.lines(path);) {
      return lines.anyMatch(line -> line.contains("pdfauthor={" + author + "}"));
    } catch (IOException e) {
      throw new MigrationException(e);
    }
  }

  private static void copy(Path src, Path dest) {
    if (Files.exists(dest)) {
      log.atWarning().log("%s already exists, skipping", dest);
      return;
    }
    try {
      log.atInfo().log("copy %s%n  to %s", src, dest);
      FileSystemUtils.copyRecursively(src, dest);
    } catch (IOException e) {
      throw new MigrationException(e);
    }
  }

  private static Path createDir(Path parent, String name) {
    Path companyFolder = parent.resolve(name);
    if ((Files.exists(companyFolder) && Files.isDirectory(companyFolder))) {
      return companyFolder;
    }

    try {
      return Files.createDirectories(companyFolder);
    } catch (IOException ioe) {
      throw new MigrationException("the folder " + companyFolder + " could not be created!", ioe);
    }
  }

  private static class MigrationException extends RuntimeException {
    MigrationException(Throwable e) {
      super("Exception whilst migrating", e);
    }

    MigrationException(String reason, Throwable e) {
      super(reason, e);
    }
  }
}
