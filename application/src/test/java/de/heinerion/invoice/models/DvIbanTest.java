package de.heinerion.invoice.models;

import de.heinerion.invoice.domain.values.DvIban;
import org.junit.jupiter.api.*;

public class DvIbanTest {
  @Test
  void isFormaitting() {
    Assertions.assertEquals("1234 5678 90", DvIban.of("1234567890").toString());
    Assertions.assertEquals("AB12 3456 7890 1234 5678 90", DvIban.of("AB12345678901234567890").toString());
    Assertions.assertEquals("AB12 3456 7890 1234 5678 90", DvIban.of("AB12 3456 7890 1234 5678 90").toString());
    Assertions.assertEquals("AB12", DvIban.of("AB12").toString());
  }

  @Test
  void isValid() {
    Assertions.assertFalse(DvIban.of("1234567890").isValid());
    Assertions.assertTrue(DvIban.of("AB12 3456 7890 1234 5678 90").isValid());
  }
}