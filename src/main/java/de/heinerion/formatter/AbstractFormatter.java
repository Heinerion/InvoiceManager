package de.heinerion.formatter;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

  protected void optionalOut(String message) {
    if (null != message && !"null".equals(message)) {
      this.out(message);
    }
  }

  protected final void out(String message) {
    this.output.add(message);
  }
}
