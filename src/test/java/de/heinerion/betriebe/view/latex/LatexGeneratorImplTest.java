package de.heinerion.betriebe.view.latex;

import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.view.formatter.Formatter;

public class LatexGeneratorImplTest extends LatexGeneratorTest {
  protected LatexGenerator getLatexGenerator() {
    Formatter formatter = ConfigurationService.getBean(Formatter.class);
    return new LatexGeneratorImpl(formatter);
  }
}