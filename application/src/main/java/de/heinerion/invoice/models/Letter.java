package de.heinerion.invoice.models;

import de.heinerion.util.Strings;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor()
@Entity
@Table(name = "letter")
public class Letter implements Conveyable<Letter> {
  public static final String LINE_SEPARATOR = "##/##";
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;
  private LocalDate date;

  private String subject;

  @Column(name = "message")
  private String messageLinesCombined;
  @ManyToOne
  @JoinColumn(name = "receiver_id")
  private Address receiver;

  public Letter(LocalDate date, Company company, Address receiver) {
    this.date = date;
    this.company = company;
    this.receiver = receiver;
  }

  public void addMessageLine(String newLine) {
    messageLinesCombined = (messageLinesCombined == null)
        ? newLine
        : messageLinesCombined + LINE_SEPARATOR + newLine;
  }

  private String getMessageLinesCombined() {
    // to hide the method explicitly
    return Optional.ofNullable(messageLinesCombined).orElse("");
  }

  private void setMessageLinesCombined(String messageLinesCombined) {
    // to hide the method explicitly
    this.messageLinesCombined = Optional.ofNullable(messageLinesCombined).orElse("");
  }

  public List<String> getMessageLines() {
    return Arrays.stream(Optional
            .ofNullable(messageLinesCombined)
            .orElse("")
            .split(LINE_SEPARATOR))
        .toList();
  }

  public void setMessageLines(List<String> lines) {
    this.messageLinesCombined = lines == null ? "" : String.join(LINE_SEPARATOR, lines);
  }

  @Override
  public boolean isPrintable() {
    final boolean hasSubject = Strings.isNotBlank(subject);
    final boolean hasContent = Strings.isNotBlank(messageLinesCombined);
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
        Objects.equals(messageLinesCombined, letter.messageLinesCombined) &&
        Objects.equals(receiver, letter.receiver);
  }

  @Override
  public int hashCode() {
    return Objects.hash(company, date, subject, messageLinesCombined, receiver);
  }

  @Override
  public String toString() {
    return "Letter{%s - %s}".formatted(date, subject);
  }
}
