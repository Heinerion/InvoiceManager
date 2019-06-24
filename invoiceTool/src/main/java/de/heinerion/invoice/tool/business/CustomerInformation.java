package de.heinerion.invoice.tool.business;

import de.heinerion.invoice.tool.domain.Euro;
import de.heinerion.invoice.tool.domain.Invoice;
import de.heinerion.invoice.tool.domain.InvoiceItem;
import de.heinerion.invoice.tool.domain.Letter;

import java.util.Collection;

/**
 * Represents bundled information about letters and invoices.<br>
 * Intended to be used for a customer overview
 */
public class CustomerInformation {
  private final Collection<Letter> letters;
  private final Collection<Invoice> invoices;

  public CustomerInformation(Collection<Letter> letters, Collection<Invoice> invoices) {
    this.letters = letters;
    this.invoices = invoices;
  }

  public Collection<Invoice> getInvoices() {
    return invoices;
  }

  public Collection<Letter> getLetters() {
    return letters;
  }

  public Euro getInvoiceTotal() {
    return invoices.stream()
        .map(Invoice::getItems)
        .flatMap(Collection::stream)
        .map(InvoiceItem::getGrossPrice)
        .reduce(Euro.ZERO, Euro::add);
  }
}
