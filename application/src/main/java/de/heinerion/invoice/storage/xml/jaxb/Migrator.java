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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
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
public class Migrator {
  private static final Logger logger = LogManager.getLogger(Migrator.class);

  public static void main(String... args) {
    Migrator.migrateCompanies(new PathUtilNG(), DataBase.getInstance());

    ConfigurationService.exitApplication();
  }

  public static void migrateCompanies(PathUtilNG pathUtil, DataBase dataBase) {
    IO io = ConfigurationService.getBean(IO.class);
    dataBase.setIo(io);
    dataBase.load();

    // transfer companies to new approach
    List<Company> availableCompanies = new ArrayList<>(Session.getAvailableCompanies());
    availableCompanies.sort(Company::compareTo);
    logger.info("Companies found: " + availableCompanies);

    File appBase = new File(pathUtil.getBaseDir());
    File companiesXmlFile = new File(appBase, "companies.xml");
    new CompanyManager().marshal(availableCompanies, companiesXmlFile);
    print(String.format("Available companies written to %s", companiesXmlFile.getAbsolutePath()));

    availableCompanies.forEach(company -> migrateCompanyInfo(pathUtil, dataBase, company));
  }

  private static void print(String message) {
    System.out.println(message);
  }

  private static void migrateCompanyInfo(PathUtilNG pathUtil, DataBase dataBase, Company company) {
    logger.info("Migrate " + company);
    File companyDir = createDir(pathUtil.getBaseDir(), company.getDescriptiveName());
    logCreation(companyDir);

    transferTemplatesAndAddresses(dataBase, company, companyDir);

    copyLettersAndInvoices(pathUtil, company, companyDir);
  }

  private static void logCreation(File createdFile) {
    logger.info("created " + createdFile);
  }

  private static void transferTemplatesAndAddresses(DataBase dataBase, Company company, File companyDir) {
    File templatesXmlFile = new File(companyDir, "templates.xml");
    new TemplateManager().marshal(dataBase.getTemplates(company), templatesXmlFile);
    logCreation(templatesXmlFile);

    File addressesXmlFile = new File(companyDir, "addresses.xml");
    new AddressManager().marshal(dataBase.getAddresses(), addressesXmlFile);
    logCreation(addressesXmlFile);
  }

  private static void copyLettersAndInvoices(PathUtilNG pathUtil, Company company, File companyDir) {
    File home = new File(pathUtil.getBaseDir());

    logger.info("Move letters");
    String letterDirName = ConfigurationService.get(FOLDER_LETTERS);
    File oldLetterDir = new File(home, letterDirName);
    File newLetterDir = createDir(companyDir, letterDirName);

    copyFiles(company, oldLetterDir, newLetterDir);

    logger.info("Move letter sources");
    String systemDirName = ConfigurationService.get(FOLDER_SYSTEM);
    File systemDir = new File(home, systemDirName);
    File oldLetterSrcDir = new File(systemDir, letterDirName);

    String srcDirName = ConfigurationService.get(FOLDER_SOURCES);
    File newLetterSrcDir = createDir(newLetterDir, srcDirName);

    copyFiles(company, oldLetterSrcDir, newLetterSrcDir);

    logger.info("Move invoices");
    String invoiceDirName = ConfigurationService.get(FOLDER_INVOICES);
    String invoiceIdentifier = invoiceDirName + File.separator + company.getDescriptiveName();
    File oldInvoiceDir = new File(home, invoiceIdentifier);
    File newInvoiceDir = createDir(companyDir, invoiceDirName);
    if (oldInvoiceDir.exists()) {
      copy(oldInvoiceDir, newInvoiceDir);
    }

    logger.info("Move invoice sources");
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
      print(String.format("copy %s\n  to %s", src, dest));
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
