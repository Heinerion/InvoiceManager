package de.heinerion.betriebe.loading.jaxb;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.loading.IO;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.util.PathUtil;

import java.io.File;
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
      String companyBase = PathUtil.getBaseDir() + File.separator + company.getDescriptiveName();

      // transfer templates and addresses
      File templatesXmlFile = new File(companyBase + File.separator + "templates.xml");
      new TemplateManager().marshal(DataBase.getTemplates(company), templatesXmlFile);

      File addressesXmlFile = new File(companyBase + File.separator + "addresses.xml");
      new AddressManager().marshal(DataBase.getAddresses(), addressesXmlFile);
    }

    ConfigurationService.exitApplication();
  }
}
