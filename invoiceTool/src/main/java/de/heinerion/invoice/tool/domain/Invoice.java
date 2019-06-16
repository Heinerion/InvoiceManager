package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.boundary.Translator;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents an invoice with its items
 */
public class Invoice extends Document {
  private final Collection<InvoiceItem> items = new HashSet<>();

  public Invoice(Company company, String id) {
    super(company, Translator.translate("invoice"));
  }

  public void add(InvoiceItem item) {
    items.add(item);
  }

  public Collection<InvoiceItem> getItems() {
    return items;
  }

  public Euro getSum() {
    return items.stream()
        .map(InvoiceItem::getPrice)
        .reduce(Euro.ZERO, Euro::add);
  }

  @Override
  public Collection<String> getKeywords() {
    Collection<String> result = super.getKeywords();
    result.add(getSum().toString());
    return result;
  }
}
