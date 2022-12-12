package de.heinerion.invoice.domain.values;

import lombok.*;

import javax.persistence.*;
import java.util.*;
import java.util.regex.Pattern;

@Getter
@Embeddable
@NoArgsConstructor
public class DvIban {
  private static final Pattern IBAN_PATTERN = Pattern.compile("[A-Z]{2}[0-9]{2} ([0-9]{4} ){4}[0-9]{2}");

  private String iban;
  @Transient
  private String formatted;

  public static DvIban of(String iban) {
    return new DvIban().setIban(iban);
  }

  public DvIban setIban(String iban) {
    this.iban = removeAllWhitespaces(iban);
    this.formatted = format(this.iban);
    return this;
  }

  private String removeAllWhitespaces(String string) {
    return Optional.ofNullable(string)
        .map(String::strip)
        .map(s -> s.replaceAll("\\s", ""))
        .orElse(null);
  }

  public String getFormatted() {
    if (formatted == null) {
      formatted = format(iban);
    }
    return formatted;
  }

  private String format(String string) {
    return string == null
        ? null
        : String.join(" ", splitInGroups(string, 4));
  }

  public static List<String> splitInGroups(String input, int splitLength) {
    int inputLength = input.length();
    List<String> tokens = new ArrayList<>();

    int wholeTokenCount = inputLength / splitLength;
    for (int i = 0; i < wholeTokenCount; i++) {
      int start = i * splitLength;
      int end = start + splitLength;
      tokens.add(input.substring(start, end));
    }

    tokens.add(input.substring(wholeTokenCount * splitLength, inputLength));
    return tokens;
  }

  @Transient
  public boolean isValid() {
    return IBAN_PATTERN.matcher(getFormatted()).matches();
  }

  @Override
  public String toString() {
    return getFormatted();
  }
}
