package de.heinerion.invoice.print.pdf.boundary;

import de.heinerion.betriebe.TestContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ProcessRunnerMock extends ProcessRunner {
  @Autowired
  ProcessRunnerMock() {
    super();
  }

  @Override
  void startProcess(String errorLogMessage, String... command) {
    TestContext.addMessage("call [" + String.join(" ") + "]");
  }
}
