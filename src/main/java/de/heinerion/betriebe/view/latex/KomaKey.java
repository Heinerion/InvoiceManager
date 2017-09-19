package de.heinerion.betriebe.view.latex;

enum KomaKey {
  SIGNATURE, SUBJECT, FROMADDRESS, FROMPHONE, FROMNAME;

  @Override
  public String toString() {
    return this.name().toLowerCase();
  }
}