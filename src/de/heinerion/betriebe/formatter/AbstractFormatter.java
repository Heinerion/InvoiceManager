package de.heinerion.betriebe.formatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;

public abstract class AbstractFormatter implements Formatter {
  private final List<String> output;

  AbstractFormatter() {
    this.output = new ArrayList<>();
  }

  @Override
  public abstract void formatAddress(Address address);

  @Override
  public abstract void formatDate(LocalDate date);

  @Override
  public abstract void formatInvoice(Invoice invoice);

  @Override
  public abstract void formatLetter(Letter letter);

  @Override
  public final List<String> getOutput() {
    return this.output;
  }

  final void optionalOut(String message) {
    if (null != message && !"null".equals(message)) {
      this.out(message);
    }
  }

  final void out(String message) {
    this.output.add(message);
  }
}
