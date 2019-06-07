package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.invoice.view.formatter.Formatter;

public class LatexGeneratorFreeMarkerImplTest extends LatexGeneratorTest {
  protected LatexGenerator getLatexGenerator() {
    Formatter formatter = ConfigurationService.getBean(Formatter.class);
    return new LatexGeneratorFreeMarkerImpl(formatter);
  }
}