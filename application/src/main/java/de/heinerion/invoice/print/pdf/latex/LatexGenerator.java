package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.invoice.models.Conveyable;

public interface LatexGenerator {
  String generateSourceContent(Conveyable conveyable);
}
