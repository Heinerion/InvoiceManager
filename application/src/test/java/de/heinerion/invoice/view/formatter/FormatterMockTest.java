package de.heinerion.invoice.view.formatter;

import de.heinerion.invoice.testsupport.builder.AddressBuilder;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class FormatterMockTest {
  @Test
  public void formatAddress() {
    List<String> expected = new ArrayList<>();
    expected.add("formatted");
    expected.add("address");

    assertEquals(expected, ((Formatter) new FormatterMock()).formatAddress(new AddressBuilder().build()));
  }
}