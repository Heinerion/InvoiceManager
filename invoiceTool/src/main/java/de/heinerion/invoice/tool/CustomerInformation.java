package de.heinerion.invoice.tool;

import java.util.Collection;

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
        .map(InvoiceItem::getPrice)
        .reduce(Euro.ZERO, Euro::add);
  }
}
