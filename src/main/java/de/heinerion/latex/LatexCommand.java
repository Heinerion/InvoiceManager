package de.heinerion.latex;

public final class LatexCommand {
  private boolean renew;
  private String name;
  private String value;

  private LatexCommand(String aName, String aValue) {
    this.name = aName;
    this.value = aValue;
  }

  LatexCommand(String aName, String aValue, boolean isRenew) {
    this(aName, aValue);
    setRenew(isRenew);
  }

  private void setRenew(boolean isRenew) {
    this.renew = isRenew;
  }

  @Override
  public String toString() {
    final String command = "\\" + (renew ? "re" : "") + "newcommand";
    final String argName = Syntax.embrace(name);
    final String argValue = Syntax.embrace(value);

    return command + argName + argValue;
  }
}
