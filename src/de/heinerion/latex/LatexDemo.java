package de.heinerion.latex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.heinerion.betriebe.classes.fileOperations.Syntax;

public final class LatexDemo {
  private static Logger logger = LogManager.getLogger(LatexDemo.class);

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

    logger.debug(letter);
    letter.setCompressed(true);
    logger.debug(letter);
    logger.debug(preamble);
  }
}