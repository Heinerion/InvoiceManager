package de.heinerion.latex;

import de.heinerion.betriebe.classes.fileOperations.Syntax;

public abstract class AbstractLatexContent {
  private static String tab = "";

  protected final String indent(String text, String indentation) {
    final String[] lines = text.split(Syntax.EOL);
    final String result = String.join(Syntax.EOL + indentation, lines);
    return indentation + result;
  }

  protected final String indent(String text) {
    return indent(text, tab);
  }

  public static void setTab(String tabSymbol) {
    tab = tabSymbol;
  }
}
