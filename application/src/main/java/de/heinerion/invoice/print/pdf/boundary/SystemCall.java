package de.heinerion.invoice.print.pdf.boundary;

import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.File;

@Flogger
@Service
@RequiredArgsConstructor
class SystemCall {
  private final ProcessRunner processRunner;

  void pdfLatex(File tex) {
    String program = "pdflatex";
    String fileArgument = processRunner.quote(tex.getAbsolutePath());

    log.atFine().log("command '%s %s'", program, fileArgument);

    String errorLogMessage = Boundary.translate("error.pdflatex");
    processRunner.startProcess(errorLogMessage, program, fileArgument);
  }
}
