package de.heinerion.betriebe.loading;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;

import java.io.IOException;
import java.util.List;

public interface Serializer {
  void saveAddresses(List<Address> addresses)
      throws IOException;

  void saveCompanies(List<Company> companies)
      throws IOException;

  void saveInvoices(List<Invoice> invoices)
      throws IOException;

  void saveLetters(List<Letter> letters)
      throws IOException;
}
