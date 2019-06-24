package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.boundary.Translator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

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

  public Euro getNetSum() {
    return items.stream()
        .map(InvoiceItem::getNetPrice)
        .reduce(Euro.ZERO, Euro::add);
  }

  public Euro getGrossSum() {
    return items.stream()
        .map(InvoiceItem::getGrossPrice)
        .reduce(Euro.ZERO, Euro::add);
  }

  public Map<Percent, Euro> getTaxes() {
    return items.stream().collect(
        Collectors.groupingBy(InvoiceItem::getTaxPercentage,
            Collectors.mapping(InvoiceItem::getTaxes,
                Collectors.reducing(Euro.ZERO, Euro::add))));
  }

  @Override
  public Collection<String> getKeywords() {
    Collection<String> result = super.getKeywords();
    result.add(getNetSum().toString());
    return result;
  }

  @Override
  public String toString() {
    return getSubject() + " from " + getCompany() + " to " + getCustomer();
  }

  @Override
  public Document copy() {
    Invoice result = new Invoice(getCompany(), "");
    copyDocumentPropertiesTo(result);
    items.forEach(result::add);
    return result;
  }
}
