package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.repositories.company.CompanyManager;
import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyManagerTest {
  private CompanyManager manager;
  private List<Company> companies;

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
    manager = new CompanyManager();
    manager.setBeautify(true);

    Path out = Path.of("companiesBeauty.xml");
    manager.marshal(companies, out);

    List<Company> result = manager.unmarshal(out);
    Files.delete(out);

    Assert.assertEquals(map(companies), map(result));
  }

  private List<String> map(List<Company> list) {
    return list.stream()
        .map(Company::toString)
        .collect(Collectors.toList());
  }

  @Test
  public void roundTripUgly() throws Exception {
    manager = new CompanyManager();

    Path out = Path.of("companiesUgly.xml");
    manager.marshal(companies, out);

    List<Company> result = manager.unmarshal(out);
    Files.delete(out);

    Assert.assertEquals(map(companies), map(result));
  }
}