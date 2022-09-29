package de.heinerion.invoice.view.swing.menu.tablemodels.letters;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.models.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Locale;
import java.util.function.Function;

@RequiredArgsConstructor
public enum LetterColumn {
  SUBJECT(String.class, Letter::getSubject),
  RECEIVER(Address.class, Letter::getReceiver),
  DATE(LocalDate.class, Letter::getDate);

  private final Class<?> columnClass;
  private final Function<Letter, ?> getter;

  public String getName() {
    return Translator.translate("table." + name().toLowerCase(Locale.ROOT));
  }

  public Class<?> getColumnClass() {
    return columnClass;
  }

  public boolean isEditable() {
    return false;
  }

  public Object getValue(Letter letter) {
    return getter.apply(letter);
  }
}
