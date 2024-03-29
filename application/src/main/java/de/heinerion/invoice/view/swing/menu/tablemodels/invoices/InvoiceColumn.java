package de.heinerion.invoice.view.swing.menu.tablemodels.invoices;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.models.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum InvoiceColumn {
  NUMBER(Integer.class, Invoice::getNumber),
  RECEIVER(Address.class, Conveyable::getReceiver),
  PRODUCT(String.class, invoice -> invoice.getItems().stream()
      .map(Item::toString)
      .collect(Collectors.joining(", "))),
  DATE(LocalDate.class, Conveyable::getDate),
  SENDER(Company.class, Conveyable::getCompany),
  AMOUNT(Double.class, Invoice::getGross);

  private final Class<?> columnClass;
  private final Function<Invoice, ?> getter;

  public String getName() {
    return Translator.translate("table." + name().toLowerCase(Locale.ROOT));
  }

  public Class<?> getColumnClass() {
    return columnClass;
  }

  public boolean isEditable() {
    return false;
  }

  public Object getValue(Invoice invoice) {
    return getter.apply(invoice);
  }
}
