package de.heinerion.betriebe.loader;

import java.io.IOException;
import java.util.List;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;

@Deprecated
public interface Loader {
  List<Address> loadAddresses(String path);

  List<Company> loadCompanies(String path) throws IOException;

  List<Invoice> loadInvoices(String path) throws IOException;

  List<Letter> loadLetters(String path) throws IOException;

  void saveAddresses(List<Address> list, String path) throws IOException;

  void saveCompanies(List<Company> list, String path) throws IOException;

  void saveInvoices(List<Invoice> list, String path) throws IOException;

  void saveLetters(List<Letter> list, String path) throws IOException;

}
