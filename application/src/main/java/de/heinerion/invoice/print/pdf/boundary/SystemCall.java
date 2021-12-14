package de.heinerion.invoice.print.pdf.boundary;

import lombok.extern.flogger.Flogger;

import java.io.File;

@Flogger
class SystemCall {
  private final ProcessRunner processRunner;

  SystemCall(ProcessRunner processRunner) {
    this.processRunner = processRunner;
  }

  void pdfLatex(File tex) {
    String program = "pdflatex";
    String fileArgument = processRunner.quote(tex.getAbsolutePath());

    log.atInfo().log("command '%s %s'", program, fileArgument);

    String errorLogMessage = Boundary.translate("error.pdflatex");
    processRunner.startProcess(errorLogMessage, program, fileArgument);
  }
}
