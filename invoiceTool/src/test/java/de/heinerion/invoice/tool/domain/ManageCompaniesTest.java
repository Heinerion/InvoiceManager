package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.boundary.DataStore;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;

public class ManageCompaniesTest {
  private DataStore dataStore;

  @Before
  public void setUp() {
    dataStore = new DataStore();
  }

  @Test
  public void addNewCompany() {
    Company newCompany = createDefaultCompany();
    dataStore.save(newCompany);

    Collection<Company> companys = dataStore.getCompanys();
    assertTrue("the new company was saved", companys.contains(newCompany));

    Company loadedCompany = dataStore.getCompany("ACME").orElse(null);
    assertEquals("The loaded company is equal to the original one", newCompany, loadedCompany);
    assertEquals("ACME", loadedCompany.getName());
    assertEquals(Arrays.asList("some road 75", "some city"), loadedCompany.getAddress());
    assertEquals(Optional.of("Mr. Anderson"), loadedCompany.getCorrespondent());
  }

  @Test
  public void editCompany() {
    Company newCompany = createDefaultCompany();
    dataStore.save(newCompany);

    Company loadedCompany = dataStore.getCompany("ACME").orElse(null);
    assertEquals("The original correspondent is set", Optional.of("Mr. Anderson"), loadedCompany.getCorrespondent());

    newCompany.setCorrespondent("Mr. Snow");
    dataStore.save(newCompany);

    loadedCompany = dataStore.getCompany("ACME").orElse(null);
    assertEquals("The changed correspondent is set", Optional.of("Mr. Snow"), loadedCompany.getCorrespondent());
  }

  @Test
  public void deleteCompany() {
    Company newCompany = createDefaultCompany();
    dataStore.save(newCompany);

    assertTrue("The company was saved", dataStore.getCompanys().contains(newCompany));

    assertTrue("The company was removed", dataStore.delete(newCompany));
    assertFalse("No traces are left", dataStore.getCompanys().contains(newCompany));
  }

  private Company createDefaultCompany() {
    Company company = new Company("ACME");
    company.setAddress("some road 75", "some city");
    company.setCorrespondent("Mr. Anderson");
    return company;
  }
}
