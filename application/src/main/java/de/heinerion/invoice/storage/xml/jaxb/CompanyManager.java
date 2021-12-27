package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.models.Company;

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
  protected Class<?> getWrapper() {
    return CompanyListWrapper.class;
  }
}
