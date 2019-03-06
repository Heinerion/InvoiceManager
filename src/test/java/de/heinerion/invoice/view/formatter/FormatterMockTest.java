package de.heinerion.invoice.view.formatter;

import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.invoice.testsupport.builder.AddressBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FormatterMockTest {
  private Formatter formatter;

  @Before
  public void setUp() {
    formatter = ConfigurationService.getBean(Formatter.class);
  }

  @Test
  public void formatAddress() {
    List<String> expected = new ArrayList<>();
    expected.add("formatted");
    expected.add("address");

    assertEquals(expected, formatter.formatAddress(new AddressBuilder().build()));
  }
}