package de.heinerion.latex;

import de.heinerion.betriebe.classes.file_operations.Syntax;

public final class KomaVar {
  private static final String COMMAND = "\\setkomavar";

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
    final String argName = Syntax.embrace(key);
    final String argValue = Syntax.embrace(value);

    return COMMAND + argName + argValue;
  }
}
