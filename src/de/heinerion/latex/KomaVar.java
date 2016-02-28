package de.heinerion.latex;

import de.heinerion.betriebe.classes.fileOperations.Syntax;

public final class KomaVar {
  private KomaKey key;
  private String value;

  public KomaVar(KomaKey aKey, String aValue) {
    this.key = aKey;
    this.value = aValue;
  }

  public KomaKey getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    final String command = "\\setkomavar";
    final String argName = Syntax.embrace(key);
    final String argValue = Syntax.embrace(value);

    return command + argName + argValue;
  }
}
