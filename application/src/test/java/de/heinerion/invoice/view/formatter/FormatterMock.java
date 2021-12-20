package de.heinerion.invoice.view.formatter;

import de.heinerion.betriebe.models.Address;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Profile("Test")
@Primary
public class FormatterMock extends Formatter {
  @Override
  public List<String> formatAddress(Address address) {
    return Arrays.asList("formatted", "address");
  }
}
