package de.heinerion.invoice.testsupport.builder;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Letter;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

public class LetterBuilder implements TestDataBuilder<Letter> {
  private Company company;
  private LocalDate date;

  private String subject;

  private List<String> messageLines;
  private Address receiver;

  protected Optional<Company> getCompany() {
    return Optional.ofNullable(company);
  }

  public LetterBuilder withCompany(Company company) {
    this.company = company;
    return this;
  }

  public LetterBuilder withCompany(CompanyBuilder builder) {
    withCompany(builder.build());
    return this;
  }

  protected Optional<LocalDate> getDate() {
    return Optional.ofNullable(date);
  }

  public LetterBuilder withDate(LocalDate date) {
    this.date = date;
    return this;
  }

  public LetterBuilder withDateOf(int year, Month month, int dayOfMonth) {
    withDate(LocalDate.of(year, month, dayOfMonth));
    return this;
  }

  protected Optional<String> getSubject() {
    return Optional.ofNullable(subject);
  }

  public LetterBuilder withSubject(String subject) {
    this.subject = subject;
    return this;
  }

  protected Optional<List<String>> getMessageLines() {
    return Optional.ofNullable(messageLines);
  }

  public LetterBuilder withMessageLines(List<String> messageLines) {
    this.messageLines = messageLines;
    return this;
  }

  protected Optional<Address> getReceiver() {
    return Optional.ofNullable(receiver);
  }

  public LetterBuilder withReceiver(Address receiver) {
    this.receiver = receiver;
    return this;
  }

  public LetterBuilder withReceiver(AddressBuilder builder) {
    withReceiver(builder.build());
    return this;
  }

  @Override
  public Letter build() {
    return null;
  }
}
