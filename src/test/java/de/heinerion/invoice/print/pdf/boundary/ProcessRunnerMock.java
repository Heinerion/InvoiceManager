package de.heinerion.invoice.print.pdf.boundary;

public class ProcessRunnerMock extends ProcessRunner {
  ProcessRunnerMock() {
    super();
  }

  @Override
  void startProcess(String errorLogMessage, String... command) {
    TestContext.addMessage("call [" + String.join(" ") + "]");
  }
}
