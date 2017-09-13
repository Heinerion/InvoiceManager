package de.heinerion.betriebe.builder;

import de.heinerion.betriebe.models.Invoice;

import java.time.LocalDate;

public class InvoiceBuilder extends LetterBuilder {
  @Override
  public Invoice build() {
    return new Invoice(
        getDate().orElse(LocalDate.now()),
        getCompany().orElse(new CompanyBuilder().build()),
        getReceiver().orElse(new AddressBuilder().build())
    );
  }
}
