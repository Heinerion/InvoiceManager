package de.heinerion.invoice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Letter implements Conveyable {
  private Company company;
  private LocalDate date;

  private String subject;

  private List<String> messageLines = new ArrayList<>();
  private Address receiver;

  public Letter(LocalDate date, Company company, Address receiver) {
    this.date = date;
    this.company = company;
    this.receiver = receiver;
  }

  public void addMessageLine(String messageLine) {
    this.messageLines.add(messageLine);
  }

  @Override
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
