package de.heinerion.betriebe.formatter;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;

import java.time.LocalDate;
import java.util.List;

public interface Formatter {
  void formatAddress(Address address);

  void formatDate(LocalDate date);

  void formatInvoice(Invoice invoice);

  void formatLetter(Letter letter);

  List<String> getOutput();
}
