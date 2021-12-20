package de.heinerion.invoice.print.pdf.latex;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class LatexGeneratorFreeMarkerImplTest extends LatexGeneratorTest {
  @Autowired
  @Qualifier("FreemarkerImpl")
  private LatexGenerator latexGenerator;

  protected LatexGenerator getLatexGenerator() {
    return latexGenerator;
  }
}

