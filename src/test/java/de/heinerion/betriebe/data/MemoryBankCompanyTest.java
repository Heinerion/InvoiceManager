package de.heinerion.betriebe.data;

import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MemoryBankCompanyTest {
  private Company company;
  private MemoryBank memoryBank;

  @Before
  public void setUp() {
    memoryBank = new MemoryBank();
    company = new CompanyBuilder().build();
  }

  @Test
  public void testAddCompany() {
    memoryBank.addCompany(company);

    assertTrue(memoryBank.getAllCompanies().contains(company));
  }

  @Test
  public void testAddCompanyMultipleTimes() {
    int baseSize = memoryBank.getAllCompanies().size();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    memoryBank.addCompany(company);
    memoryBank.addCompany(company);

    assertEquals(expectedSize, memoryBank.getAllCompanies().size());
  }

  @Test
  public void testDoNotFindAddressByCompany() {
    assertFalse(memoryBank.getCompany("dummy").isPresent());
  }

  @Test
  public void testFindAddressByCompany() {
    memoryBank.addCompany(company);
    Optional<Company> result = memoryBank.getCompany(company.getDescriptiveName());
    assertTrue(result.isPresent());
    assertEquals(company, result.get());
  }
}