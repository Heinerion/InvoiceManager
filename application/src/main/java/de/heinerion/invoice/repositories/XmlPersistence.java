package de.heinerion.invoice.repositories;

import de.heinerion.contract.Contract;
import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.address.AddressManager;
import de.heinerion.invoice.repositories.company.CompanyManager;
import de.heinerion.invoice.repositories.invoice.InvoiceManager;
import de.heinerion.invoice.repositories.letter.LetterManager;
import de.heinerion.invoice.repositories.template.TemplateManager;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.util.*;

@Service
public class XmlPersistence {
  public List<Address> readAddresses(Path source) {
    return read(new AddressManager(), source);
  }

  public void writeAddresses(Path destination, List<Address> addresses) {
    write(new AddressManager(), addresses, destination);
  }

  public List<Company> readCompanies(Path source) {
    return read(new CompanyManager(), source);
  }

  public void writeCompanies(Path destination, List<Company> companies) {
    write(new CompanyManager(), companies, destination);
  }

  public List<InvoiceTemplate> readTemplates(Path source) {
    return read(new TemplateManager(), source);
  }

  public void writeTemplates(Path destination, List<InvoiceTemplate> templates) {
    write(new TemplateManager(), templates, destination);
  }

  public List<Invoice> readInvoices(Path source) {
    return read(new InvoiceManager(), source);
  }

  public void writeInvoices(Path destination, List<Invoice> invoices) {
    write(new InvoiceManager(), invoices, destination);
  }

  public List<Letter> readLetters(Path source) {
    return read(new LetterManager(), source);
  }

  public void writeLetters(Path destination, List<Letter> letters) {
    write(new LetterManager(), letters, destination);
  }

  private static <T> List<T> read(JaxbManager<T> manager, Path source) {
    if (Files.exists(source)) {
      return manager.unmarshal(source);
    }

    return Collections.emptyList();
  }

  private static <T> void write(JaxbManager<T> manager, List<T> entries, Path destination) {
    Contract.require(destination.toString().endsWith("xml"), "destination '%s' ends with '.xml'".formatted(destination));
    manager
        .withFormattedOutput()
        .marshal(entries, destination);
  }
}
