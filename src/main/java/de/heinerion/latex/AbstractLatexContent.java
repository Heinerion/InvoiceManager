package de.heinerion.latex;

abstract class AbstractLatexContent {
  String indent(String text) {
    final String[] lines = text.split(Syntax.EOL);
    return String.join(Syntax.EOL, lines);
  }
}
