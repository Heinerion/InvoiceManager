package de.heinerion.latex;

import de.heinerion.betriebe.fileoperations.Syntax;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LatexDemo {
  private static final Logger LOGGER = LogManager.getLogger(LatexDemo.class);

  private LatexDemo() {

  }

  public static void main(String[] args) {
    final String preamble = "\\documentclass[" + "fontsize=12pt," + "paper=a4,"
        + "fromalign=center," + "fromphone=true" + "]{scrlttr2}" + Syntax.EOL
        + "\\usepackage[utf8]{inputenc}" + Syntax.EOL
        + "\\usepackage[ngermanb]{babel}" + Syntax.EOL
        + "\\usepackage[right]{eurosym}" + Syntax.EOL;

    final LatexScrLetter letter = new LatexScrLetter("Olaf Schubert");
    letter.addTypeArgument("fontsize", "12pt");
    letter.addTypeArgument("paper", "a4");
    letter.addTypeArgument("fromalign", "center");
    letter.addTypeArgument("fromphone", "true");

    letter.addPackage("inputenc", "utf8");
    letter.addPackage("babel", "ngermanb");
    letter.addPackage("eurosym", "right");

    LOGGER.debug(letter);
    letter.setCompressed(true);
    LOGGER.debug(letter);
    LOGGER.debug(preamble);
  }
}
