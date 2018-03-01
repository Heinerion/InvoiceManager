package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.betriebe.models.Letter;

public interface LatexGenerator {
  String generateSourceContent(Letter letter);
}
