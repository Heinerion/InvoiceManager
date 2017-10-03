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

    System.out.println(DataBase.getAddresses().size());

    List<Company> availableCompanies = Session.getAvailableCompanies();
    System.out.println(availableCompanies);

    String path = PathUtil.getBaseDir() + File.separator + "companies.xml";
    new CompanyManager(true).marshalCompanies(availableCompanies, new File(path));

    for (Company company : availableCompanies) {
      String companyBase = PathUtil.getBaseDir() + File.separator + company.getDescriptiveName();

      new TemplateManager().marshalTemplates(DataBase.getTemplates(company),new File(companyBase + File.separator + "templates.xml"));
      new AddressManager(true).marshalAddresses(DataBase.getAddresses(), new File(companyBase + File.separator + "addresses.xml"));

    }

    ConfigurationService.exitApplication();
  }
}
