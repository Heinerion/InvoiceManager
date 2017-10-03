package de.heinerion.betriebe.loading.jaxb;

import de.heinerion.betriebe.builder.CompanyBuilder;
import de.heinerion.betriebe.models.Company;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyManagerTest {
  CompanyManager manager;
  List<Company> companies;

  @Before
  public void setUp() throws Exception {
    companies = new ArrayList<>();
    companies.add(new CompanyBuilder().withDescriptiveName("a").build());
    companies.add(new CompanyBuilder().withDescriptiveName("b").build());
    companies.add(new CompanyBuilder().withDescriptiveName("c").build());
    companies.add(new CompanyBuilder().withDescriptiveName("d").build());
    companies.add(new CompanyBuilder().withDescriptiveName("e").build());
    companies.add(new CompanyBuilder().withDescriptiveName("f").build());
  }

  @Test
  public void roundTripBeauty() throws Exception {
    manager = new CompanyManager(true);

    File out = new File("companiesBeauty.xml");
    manager.marshalCompanies(companies, out);

    List<Company> result = manager.unmarshal(out);
    out.delete();

    Assert.assertEquals(map(companies), map(result));
  }

  private List<String> map(List<Company> list) {
    return list.stream()
        .map(Company::toString)
        .collect(Collectors.toList());
  }

  @Test
  public void roundTripUgly() throws Exception {
    manager = new CompanyManager(false);

    File out = new File("companiesUgly.xml");
    manager.marshalCompanies(companies, out);

    List<Company> result = manager.unmarshal(out);
    out.delete();

    Assert.assertEquals(map(companies), map(result));
  }
}