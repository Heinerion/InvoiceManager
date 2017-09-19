package de.heinerion.betriebe.gui.formatter;

import de.heinerion.betriebe.builder.AddressBuilder;
import de.heinerion.betriebe.services.ConfigurationService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FormatterTest {
  private Formatter formatter;

  @Before
  public void setUp() throws Exception {
    formatter = ConfigurationService.getBean(Formatter.class);
  }

  @Test
  public void formatAddress() throws Exception {
    List<String> expected = new ArrayList<>();
    expected.add("formatted");
    expected.add("address");

    assertEquals(expected, formatter.formatAddress(new AddressBuilder().build()));
  }
}