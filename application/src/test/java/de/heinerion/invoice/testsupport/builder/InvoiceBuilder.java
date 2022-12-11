package de.heinerion.invoice.testsupport.builder;

import de.heinerion.invoice.models.*;

import java.time.*;
import java.util.*;

public class InvoiceBuilder implements TestDataBuilder<Invoice> {
  private Company company;
  private LocalDate date;

  private String subject;

  private List<String> messageLines;
  private Address receiver;

  protected Optional<Company> getCompany() {
    return Optional.ofNullable(company);
  }

  public InvoiceBuilder withCompany(Company company) {
    this.company = company;
    return this;
  }

  public InvoiceBuilder withCompany(CompanyBuilder builder) {
    withCompany(builder.build());
    return this;
  }

  protected Optional<LocalDate> getDate() {
    return Optional.ofNullable(date);
  }

  public InvoiceBuilder withDate(LocalDate date) {
    this.date = date;
    return this;
  }

  public InvoiceBuilder withDateOf(int year, Month month, int dayOfMonth) {
    withDate(LocalDate.of(year, month, dayOfMonth));
    return this;
  }

  protected Optional<String> getSubject() {
    return Optional.ofNullable(subject);
  }

  public InvoiceBuilder withSubject(String subject) {
    this.subject = subject;
    return this;
  }

  protected Optional<List<String>> getMessageLines() {
    return Optional.ofNullable(messageLines);
  }

  public InvoiceBuilder withMessageLines(List<String> messageLines) {
    this.messageLines = messageLines;
    return this;
  }

  protected Optional<Address> getReceiver() {
    return Optional.ofNullable(receiver);
  }

  public InvoiceBuilder withReceiver(Address receiver) {
    this.receiver = receiver;
    return this;
  }

  public InvoiceBuilder withReceiver(AddressBuilder builder) {
    withReceiver(builder.build());
    return this;
  }

  @Override
  public Invoice build() {
    final Company sender = getCompany().orElseGet(() -> new CompanyBuilder().build());
    return new Invoice(
        getDate().orElse(LocalDate.now()),
        sender,
        getReceiver().orElseGet(() -> new AddressBuilder().build()), sender.getInvoiceNumber()
    );
  }
}
