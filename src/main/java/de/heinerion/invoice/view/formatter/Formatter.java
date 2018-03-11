package de.heinerion.invoice.view.formatter;

import de.heinerion.betriebe.models.Address;

import java.util.List;

public interface Formatter {
  List<String> formatAddress(Address address);
}
