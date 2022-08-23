package de.heinerion.betriebe.repositories.company;

import de.heinerion.betriebe.models.Company;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

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
