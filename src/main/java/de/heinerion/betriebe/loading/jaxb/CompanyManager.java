package de.heinerion.betriebe.loading.jaxb;

import de.heinerion.betriebe.models.Company;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class CompanyManager {
  private boolean beautify;

  public CompanyManager(boolean beautify) {
    this.beautify = beautify;
  }

  public void marshalCompanies(List<Company> companies, File destination) {
    try {
      JAXBContext context = JAXBContext.newInstance(CompanyListWrapper.class);
      Marshaller m = context.createMarshaller();

      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, beautify);

      CompanyListWrapper wrapper = new CompanyListWrapper();
      wrapper.setCompanies(companies);

      m.marshal(wrapper, destination);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<Company> unmarshal(File source) {
    try {
      JAXBContext context = JAXBContext.newInstance(CompanyListWrapper.class);
      Unmarshaller um = context.createUnmarshaller();

      CompanyListWrapper wrapper = (CompanyListWrapper) um.unmarshal(source);

      return wrapper.getCompanies();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
