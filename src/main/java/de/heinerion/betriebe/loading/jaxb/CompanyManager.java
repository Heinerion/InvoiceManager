package de.heinerion.betriebe.loading.jaxb;

import de.heinerion.betriebe.models.Company;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.List;

public class CompanyManager extends JaxbManager<Company> {
  @Override
  protected Object getRootObject() {
    return new CompanyListWrapper();
  }

  @Override
  protected void setContent(Object wrapper, List<Company> items) {
    ((CompanyListWrapper) wrapper).setCompanies(items);
  }

  @Override
  protected List<Company> getContent(Object rootObject) {
    return ((CompanyListWrapper) rootObject).getCompanies();
  }

  @Override
  protected JAXBContext getContext() throws JAXBException {
    return JAXBContext.newInstance(CompanyListWrapper.class);
  }
}
