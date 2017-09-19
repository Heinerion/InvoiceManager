package de.heinerion.betriebe.view.formatter;

import de.heinerion.betriebe.models.Address;

import java.util.List;

interface AddressFormatter {
  List<String> getOutput();

  AddressFormatter format(Address address);
}
