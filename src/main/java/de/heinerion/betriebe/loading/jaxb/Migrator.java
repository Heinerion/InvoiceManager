package de.heinerion.betriebe.loading.jaxb;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.loading.IO;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.util.PathUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Migrator {
  public static void main(String... args) {
    IO io = ConfigurationService.getBean(IO.class);
    io.load();

    // transfer companies to new approach
    List<Company> availableCompanies = Session.getAvailableCompanies();
    System.out.println(availableCompanies);

    String path = PathUtil.getBaseDir() + File.separator + "companies.xml";
    new CompanyManager().marshal(availableCompanies, new File(path));

    for (Company company : availableCompanies) {
      File companyDir = createDir(PathUtil.getBaseDir(), company.getDescriptiveName());

      // transfer templates and addresses
      File templatesXmlFile = new File(companyDir, "templates.xml");
      new TemplateManager().marshal(DataBase.getTemplates(company), templatesXmlFile);

      File addressesXmlFile = new File(companyDir, "addresses.xml");
      new AddressManager().marshal(DataBase.getAddresses(), addressesXmlFile);

      // copy letters and invoices
      File home = new File(PathUtil.getBaseDir());

      String letterDirName = ConfigurationService.get("folder.letters");
      File oldLetterDir = new File(home, letterDirName);
      File newLetterDir = createDir(companyDir, letterDirName);

      for (File pdf : oldLetterDir.listFiles()) {
        if (isOfCompany(company, pdf)) {
          copy(pdf, new File(newLetterDir, pdf.getName()));
        }
      }

      String invoiceDirName = ConfigurationService.get("folder.invoices");
      File oldInvoiceDir = new File(home, invoiceDirName + File.separator + company.getDescriptiveName());
      File newInvoiceDir = createDir(companyDir, invoiceDirName);
      copy(oldInvoiceDir, newInvoiceDir);
    }

    ConfigurationService.exitApplication();
  }

  private static boolean isOfCompany(Company company, File file) {
    try (PDDocument pdf = PDDocument.load(file)) {
      PDDocumentInformation info = pdf.getDocumentInformation();

      return company.getOfficialName().equals(info.getAuthor());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  private static void copy(File src, File dest) {
    try {
      FileSystemUtils.copyRecursively(src, dest);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static File createDir(String parentPath, String name) {
    return createDir(new File(parentPath), name);
  }

  private static File createDir(File parent, String name) {
    File companyFolder = new File(parent, name);
    if (!companyFolder.exists() || !companyFolder.isDirectory()) {
      companyFolder.mkdir();
    }
    return companyFolder;
  }
}
