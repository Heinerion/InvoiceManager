package de.heinerion.betriebe.gui.latex;

final class KomaVar {
  private static final String COMMAND = "\\setkomavar";

  private final KomaKey key;
  private final String value;

  KomaVar(KomaKey aKey, String aValue) {
    this.key = aKey;
    this.value = aValue;
  }

  @Override
  public String toString() {
    return COMMAND + Syntax.embrace(key) + Syntax.embrace(value);
  }
}
