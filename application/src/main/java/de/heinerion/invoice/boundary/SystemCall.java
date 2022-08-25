package de.heinerion.invoice.boundary;

import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Flogger
@Service
@RequiredArgsConstructor
class SystemCall {
  private final ProcessRunner processRunner;

  void pdfLatex(Path tex) {
    String program = "pdflatex";
    String fileArgument = processRunner.quote(tex.toAbsolutePath().toString());

    log.atFine().log("command '%s %s'", program, fileArgument);

    String errorLogMessage = Boundary.translate("error.pdflatex");
    processRunner.startProcess(errorLogMessage, program, fileArgument);
  }
}
