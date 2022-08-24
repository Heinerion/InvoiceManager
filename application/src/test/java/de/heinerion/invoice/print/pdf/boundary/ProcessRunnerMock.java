package de.heinerion.invoice.print.pdf.boundary;

import de.heinerion.invoice.util.PathUtilNG;

public class ProcessRunnerMock extends ProcessRunner {
  ProcessRunnerMock(PathUtilNG pathUtil) {
    super(pathUtil);
  }

  @Override
  void startProcess(String errorLogMessage, String... command) {
    TestContext.addMessage("call [" + String.join(" ") + "]");
  }
}
