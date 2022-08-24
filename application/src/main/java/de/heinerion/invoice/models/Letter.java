package de.heinerion.invoice.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Letter {
  private Company company;
  private LocalDate date;

  String subject;

  private List<String> messageLines;
  protected Address receiver;

  /**
   * For persistence only
   */
  public Letter() {
  }

  public Letter(LocalDate date, Company sender, Address receiver) {
    this.date = date;
    this.company = sender;
    this.receiver = receiver;

    this.messageLines = new ArrayList<>();
  }

  public void addMessageLine(String messageLine) {
    this.messageLines.add(messageLine);
  }

  public Company getCompany() {
    return this.company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public LocalDate getDate() {
    return this.date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public List<String> getMessageLines() {
    return this.messageLines;
  }

  public void setMessageLines(List<String> messageLines) {
    this.messageLines = messageLines;
  }

  public Address getReceiver() {
    return this.receiver;
  }

  public void setReceiver(Address receiver) {
    this.receiver = receiver;
  }

  public String getSubject() {
    return this.subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public boolean isPrintable() {
    final boolean hasSubject = !(subject == null || subject.trim().isEmpty());
    final boolean hasContent = !messageLines.isEmpty();
    return hasSubject && hasContent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Letter letter = (Letter) o;
    return Objects.equals(company, letter.company) &&
        Objects.equals(date, letter.date) &&
        Objects.equals(subject, letter.subject) &&
        Objects.equals(messageLines, letter.messageLines) &&
        Objects.equals(receiver, letter.receiver);
  }

  @Override
  public int hashCode() {
    return Objects.hash(company, date, subject, messageLines, receiver);
  }

  @Override
  public String toString() {
    return "Letter{%s - %s}".formatted(date, subject);
  }
}
