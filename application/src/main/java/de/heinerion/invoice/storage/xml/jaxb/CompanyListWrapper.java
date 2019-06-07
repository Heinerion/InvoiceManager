package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.models.Company;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "companies")
public class CompanyListWrapper {
  private List<Company> companies;

  @XmlElement(name = "company")
  public List<Company> getCompanies() {
    return companies;
  }

  public void setCompanies(List<Company> companies) {
    this.companies = companies;
  }
}
