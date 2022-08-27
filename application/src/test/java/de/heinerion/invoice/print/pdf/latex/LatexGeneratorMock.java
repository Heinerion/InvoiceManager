package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.invoice.models.Conveyable;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("Test")
@Primary
public class LatexGeneratorMock implements LatexGenerator {
  @Override
  public String generateSourceContent(Conveyable conveyable) {
    return "A Conveyable";
  }
}
