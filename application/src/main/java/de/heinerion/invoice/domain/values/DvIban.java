package de.heinerion.invoice.domain.values;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.regex.Pattern;

@Getter
@Embeddable
@NoArgsConstructor
public class DvIban {
  /**
   * Maximum length of a valid IBAN with proper spacing between number groups.
   * <p>
   * 5 groups * 5 (4 symbols + 1 space) + 2 digits at the end = 27
   */
  public static final int MAX_LEN = 27;
  private static final Pattern IBAN_PATTERN = Pattern.compile("[A-Z]{2}\\d{2} ?(\\d{4} ?){4}\\d{2}");

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
        : String.join(" ", splitInGroups(string, 4)).trim();
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
