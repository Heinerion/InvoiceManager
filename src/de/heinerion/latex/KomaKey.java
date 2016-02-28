package de.heinerion.latex;

public enum KomaKey {
  SIGNATURE, SUBJECT, FROMADDRESS, FROMPHONE, FROMNAME;

  @Override
  public String toString() {
    return this.name().toLowerCase();
  }
}