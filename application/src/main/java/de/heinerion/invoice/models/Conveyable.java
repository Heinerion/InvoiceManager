package de.heinerion.invoice.models;

import java.time.LocalDate;

public interface Conveyable {
  Company getCompany();

  LocalDate getDate();

  Address getReceiver();

  String getSubject();

  boolean isPrintable();

  Conveyable setReceiver(Address address);
}
