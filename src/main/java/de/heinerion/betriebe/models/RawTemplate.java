package de.heinerion.betriebe.models;

import static de.heinerion.betriebe.enums.Utilities.RECHNUNG;

import java.time.LocalDate;

import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.models.interfaces.Storable;

// TODO weiterführen
public final class RawTemplate implements Conveyable, Storable {
  // private Company company;
  // private LocalDate date;

  // private Address receiver;

  @Override
  public Company getCompany() {
    // return this.company;
    return null;
  }

  @Override
  public LocalDate getDate() {
    // return this.date;
    return null;
  }

  @Override
  public String getEntryName() {
    // return this.receiver.getNumber() + " " + this.receiver.getRecipient();
    return null;
  }

  @Override
  public Address getReceiver() {
    // return this.receiver;
    return null;
  }

  @Override
  public String getSubject() {
    return RECHNUNG.getText();
  }

  @Override
  public boolean isPrintable() {
    return true;
  }
}
