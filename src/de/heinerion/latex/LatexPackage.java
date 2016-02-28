package de.heinerion.latex;

public final class LatexPackage {
  private static final String PREFIX = "\\usepackage";

  private String name;
  private String arguments;

  LatexPackage(String aName, String someArguments) {
    this.name = aName;
    this.arguments = someArguments;
  }

  public String getArguments() {
    return arguments;
  }

  public String getName() {
    return name;
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
