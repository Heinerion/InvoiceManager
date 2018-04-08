package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.storage.loading.IO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Migrator {
  private static final Logger logger = LogManager.getLogger(Migrator.class);

  private PathUtilNG pathUtil;
  private DataBase dataBase = DataBase.getInstance();

  private Migrator(PathUtilNG pathUtil) {
    this.pathUtil = pathUtil;
  }

  public static void main(String... args) {
    new Migrator(new PathUtilNG()).run();
  }

  private void run() {
    IO io = ConfigurationService.getBean(IO.class);
    io.load();

    // transfer companies to new approach
    List<Company> availableCompanies = Session.getAvailableCompanies();
    logger.info("Companies found: " + availableCompanies);

    File appBase = new File(pathUtil.getBaseDir());
    File companiesXmlFile = new File(appBase, "companies.xml");
    new CompanyManager().marshal(availableCompanies, companiesXmlFile);

    for (Company company : availableCompanies) {
      logger.info("Migrate " + company);
      migrateCompanyInfo(company);
    }

    ConfigurationService.exitApplication();
  }

  private void migrateCompanyInfo(Company company) {
    File companyDir = createDir(pathUtil.getBaseDir(), company.getDescriptiveName());
    logCreation(companyDir);

    transferTemplatesAndAddresses(company, companyDir);

    copyLettersAndInvoices(company, companyDir);
  }

  private void logCreation(File createdFile) {
    logger.info("created " + createdFile);
  }

  private void transferTemplatesAndAddresses(Company company, File companyDir) {
    File templatesXmlFile = new File(companyDir, "templates.xml");
    new TemplateManager().marshal(dataBase.getTemplates(company), templatesXmlFile);
    logCreation(templatesXmlFile);

    File addressesXmlFile = new File(companyDir, "addresses.xml");
    new AddressManager().marshal(dataBase.getAddresses(), addressesXmlFile);
    logCreation(addressesXmlFile);
  }

  private void copyLettersAndInvoices(Company company, File companyDir) {
    File home = new File(pathUtil.getBaseDir());

    // move letters
    String letterDirName = ConfigurationService.get("folder.letters");
    File oldLetterDir = new File(home, letterDirName);
    File newLetterDir = createDir(companyDir, letterDirName);

    copyFiles(company, oldLetterDir, newLetterDir);

    // move letter sources
    String systemDirName = ConfigurationService.get("folder.system");
    File systemDir = new File(home, systemDirName);
    File oldLetterSrcDir = new File(systemDir, letterDirName);

    String srcDirName = ConfigurationService.get("folder.sources");
    File newLetterSrcDir = createDir(newLetterDir, srcDirName);

    copyFiles(company, oldLetterSrcDir, newLetterSrcDir);

    // move invoices
    String invoiceDirName = ConfigurationService.get("folder.invoices");
    String invoiceIdentifier = invoiceDirName + File.separator + company.getDescriptiveName();
    File oldInvoiceDir = new File(home, invoiceIdentifier);
    File newInvoiceDir = createDir(companyDir, invoiceDirName);
    copy(oldInvoiceDir, newInvoiceDir);

    // move invoice sources
    File oldInvoiceSrcDir = new File(systemDir, invoiceIdentifier);
    File newInvoiceSrcDir = createDir(newInvoiceDir, srcDirName);

    copyFiles(company, oldInvoiceSrcDir, newInvoiceSrcDir);
  }

  private void copyFiles(Company company, File oldLetterDir, File newLetterDir) {
    File[] letterPDFs = oldLetterDir.listFiles();
    if (letterPDFs != null) {
      Predicate<File> fileBelongsToCompany = pdf -> isOfCompany(company, pdf);
      Consumer<File> copyFileToNewLocation = pdf -> copy(pdf, new File(newLetterDir, pdf.getName()));

      Arrays.stream(letterPDFs)
          .filter(fileBelongsToCompany)
          .forEach(copyFileToNewLocation);
    }
  }

  private boolean isOfCompany(Company company, File file) {
    String author = company.getOfficialName();
    String fileName = file.getName();

    return fileName.endsWith("pdf") && pdfHasAuthor(file, author)
        || fileName.endsWith("tex") && texHasAuthor(file, author);
  }

  private boolean pdfHasAuthor(File file, String author) {
    try (PDDocument pdf = PDDocument.load(file)) {
      PDDocumentInformation info = pdf.getDocumentInformation();

      return author.equals(info.getAuthor());
    } catch (IOException e) {
      throw new MigrationException(e);
    }
  }

  private boolean texHasAuthor(File file, String author) {
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

  private void copy(File src, File dest) {
    try {
      logger.info("\n copy " + src + "\n to   " + dest);
      FileSystemUtils.copyRecursively(src, dest);
    } catch (IOException e) {
      throw new MigrationException(e);
    }
  }

  private File createDir(String parentPath, String name) {
    return createDir(new File(parentPath), name);
  }

  private File createDir(File parent, String name) {
    File companyFolder = new File(parent, name);
    if ((companyFolder.exists() && companyFolder.isDirectory()) || companyFolder.mkdir()) {
      return companyFolder;
    }
    throw new MigrationException("the folder " + companyFolder + " could not be created!");
  }

  private class MigrationException extends RuntimeException {
    MigrationException(Throwable e) {
      super("Exception whilst migrating", e);
    }

    MigrationException(String reason) {
      super(reason);
    }
  }
}
