package de.heinerion.betriebe.models.interfaces;

import java.time.LocalDate;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;

public interface Conveyable {
  Company getCompany();

  LocalDate getDate();

  Address getReceiver();

  String getSubject();

  /**
   * Bestimmt, ob ein Conveyable in einem druckbaren Zustand ist.
   * 
   * @return {@code true}, wenn das Dokument vollständig und gültig ist.
   *         {@code false} sonst.
   */
  boolean isPrintable();
}
