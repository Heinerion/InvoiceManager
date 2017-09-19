package de.heinerion.betriebe.gui.latex;

final class LatexPackage {
  private static final String PREFIX = "\\usepackage";

  private final String name;
  private final String arguments;

  LatexPackage(String aName, String someArguments) {
    this.name = aName;
    this.arguments = someArguments;
  }

  @Override
  public String toString() {
    String packageDeclaration = PREFIX;

    if (null != arguments) {
      packageDeclaration += "[" + arguments + "]";
    }

    packageDeclaration += "{" + name + "}";
    return packageDeclaration;
  }
}
