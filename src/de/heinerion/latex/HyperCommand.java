package de.heinerion.latex;

public final class HyperCommand {
  private String key;
  private String value;

  public HyperCommand(String aKey, String aValue) {
    this.key = aKey;
    this.value = aValue;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    final String argName = key;
    final String argValue = "{" + value + "}";

    final String result = argName + "=" + argValue;
    return result;
  }
}
