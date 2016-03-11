package de.heinerion.betriebe.models.interfaces;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;

import java.time.LocalDate;

public interface Conveyable {
  Company getCompany();

  LocalDate getDate();

  Address getReceiver();

  String getSubject();

  /**
   * Bestimmt, ob ein Conveyable in einem druckbaren Zustand ist.
   *
   * @return {@code true}, wenn das Dokument vollständig und gültig ist.
   * {@code false} sonst.
   */
  boolean isPrintable();
}
