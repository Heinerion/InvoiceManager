package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.models.*;
import de.heinerion.contract.Contract;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Service
public class XmlPersistence {
  public List<Address> readAddresses(File source) {
    return read(new AddressManager(), source);
  }

  public void writeAddresses(File destination, List<Address> addresses) {
    write(new AddressManager(), addresses, destination);
  }

  public List<Company> readCompanies(File source) {
    return read(new CompanyManager(), source);
  }

  public void writeCompanies(File destination, List<Company> companies) {
    write(new CompanyManager(), companies, destination);
  }

  public List<InvoiceTemplate> readTemplates(File source) {
    return read(new TemplateManager(), source);
  }

  public void writeTemplates(File destination, List<InvoiceTemplate> templates) {
    write(new TemplateManager(), templates, destination);
  }

  public List<Invoice> readInvoices(File source) {
    return read(new InvoiceManager(), source);
  }

  public void writeInvoices(File destination, List<Invoice> invoices) {
    write(new InvoiceManager(), invoices, destination);
  }

  public List<Letter> readLetters(File source) {
    return read(new LetterManager(), source);
  }

  public void writeLetters(File destination, List<Letter> letters) {
    write(new LetterManager(), letters, destination);
  }

  private static <T> List<T> read(JaxbManager<T> manager, File source) {
    if (source.exists()) {
      return manager.unmarshal(source);
    }

    return Collections.emptyList();
  }

  private static <T> void write(JaxbManager<T> manager, List<T> entries, File destination) {
    Contract.require(destination.getName().endsWith(".xml"), "destination ends with '.xml'");
    manager
        .withFormattedOutput()
        .marshal(entries, destination);

  }
}
