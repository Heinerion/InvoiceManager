package de.heinerion.invoice.view.formatter;

import de.heinerion.betriebe.models.Address;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Formatter {
  public List<String> formatAddress(Address address) {
    List<String> output = new ArrayList<>();

    output.add(address.getRecipient());
    address.getCompany().ifPresent(output::add);
    address.getDistrict().ifPresent(output::add);
    output.add(address.getStreet() + " " + address.getNumber());
    address.getApartment().ifPresent(output::add);
    output.add(address.getPostalCode() + " " + address.getLocation());

    return output;
  }
}
