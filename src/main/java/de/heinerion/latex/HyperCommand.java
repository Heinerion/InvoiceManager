package de.heinerion.latex;

final class HyperCommand {
  private final String key;
  private final String value;

  HyperCommand(String aKey, String aValue) {
    this.key = aKey;
    this.value = aValue;
  }

  @Override
  public String toString() {
    return key + "=" + Syntax.embrace(value);
  }
}
