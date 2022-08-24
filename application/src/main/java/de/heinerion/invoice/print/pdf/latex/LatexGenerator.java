package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.invoice.models.Letter;

public interface LatexGenerator {
  String generateSourceContent(Letter letter);
}
