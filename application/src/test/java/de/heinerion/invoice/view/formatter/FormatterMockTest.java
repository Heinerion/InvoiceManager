package de.heinerion.invoice.view.formatter;

import de.heinerion.invoice.testsupport.builder.AddressBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FormatterMockTest {
  @Autowired
  private Formatter formatter;

  @Test
  public void formatAddress() {
    List<String> expected = new ArrayList<>();
    expected.add("formatted");
    expected.add("address");

    assertEquals(expected, formatter.formatAddress(new AddressBuilder().build()));
  }
}