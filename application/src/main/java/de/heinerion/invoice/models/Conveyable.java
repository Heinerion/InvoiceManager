package de.heinerion.invoice.models;

import java.time.LocalDate;

public interface Conveyable<T extends Conveyable<T>> {
  Company getCompany();

  LocalDate getDate();

  Address getReceiver();

  String getSubject();

  boolean isPrintable();

  T setReceiver(Address address);
}
