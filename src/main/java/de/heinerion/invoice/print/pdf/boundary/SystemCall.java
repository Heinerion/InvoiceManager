package de.heinerion.invoice.print.pdf.boundary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

class SystemCall {
  private Logger logger = LogManager.getLogger(SystemCall.class);
  private final ProcessRunner processRunner;

  SystemCall(ProcessRunner processRunner) {
    this.processRunner = processRunner;
  }

  void pdfLatex(File tex) {
    String program = "pdflatex";
    String fileArgument = processRunner.quote(tex.getAbsolutePath());

    if (logger.isInfoEnabled()) {
      logger.info("command '{} {}'", program, fileArgument);
    }

    String errorLogMessage = Boundary.translate("error.pdflatex");
    processRunner.startProcess(errorLogMessage, program, fileArgument);
  }
}
