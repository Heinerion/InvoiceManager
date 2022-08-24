package de.heinerion.invoice.testsupport.builder;

import de.heinerion.invoice.models.Invoice;

import java.time.LocalDate;

public class InvoiceBuilder extends LetterBuilder {
  @Override
  public Invoice build() {
    return new Invoice(
        getDate().orElse(LocalDate.now()),
        getCompany().orElseGet(() -> new CompanyBuilder().build()),
        getReceiver().orElseGet(() -> new AddressBuilder().build())
    );
  }
}
