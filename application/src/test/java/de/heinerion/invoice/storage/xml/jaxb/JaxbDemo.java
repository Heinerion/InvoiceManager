package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.repositories.company.CompanyListWrapper;
import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JaxbDemo {
  public static void main(String... args) {
    JaxbDemo demo = new JaxbDemo();

    List<Company> companies = new ArrayList<>();
    companies.add(new CompanyBuilder().withDescriptiveName("a").build());
    companies.add(new CompanyBuilder().withDescriptiveName("b").build());
    companies.add(new CompanyBuilder().withDescriptiveName("c").build());
    companies.add(new CompanyBuilder().withDescriptiveName("d").build());
    companies.add(new CompanyBuilder().withDescriptiveName("e").build());
    companies.add(new CompanyBuilder().withDescriptiveName("f").build());

    String home = System.getProperty("user.home");

    File out = new File(home + "/companies.xml");
    demo.saveCompaniesToFile(companies, out, true);

    File out2 = new File(home + "/companiesX.xml");
    demo.saveCompaniesToFile(companies, out2, false);
  }

  public void saveCompaniesToFile(List<Company> companies, File file, boolean beautify) {
    try {
      // Wrapping our person data.
      CompanyListWrapper wrapper = createWrapper(companies);

      Marshaller m = createMarshaller(beautify, wrapper);

      // Marshalling and saving XML to the file.
      m.marshal(wrapper, file);
    } catch (Exception e) { // catches ANY exception
      throw new RuntimeException(e);
    }
  }

  private Marshaller createMarshaller(boolean beautify, CompanyListWrapper wrapper) throws JAXBException {
    JAXBContext context = JAXBContext
        .newInstance(wrapper.getClass());
    Marshaller m = context.createMarshaller();

    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, beautify);
    return m;
  }

  private CompanyListWrapper createWrapper(List<Company> companies) {
    CompanyListWrapper wrapper = new CompanyListWrapper();
    wrapper.setCompanies(companies);
    return wrapper;
  }
}
