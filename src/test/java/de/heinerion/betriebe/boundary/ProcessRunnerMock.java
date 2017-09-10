package de.heinerion.betriebe.boundary;

import de.heinerion.betriebe.TestContext;
import de.heinerion.betriebe.services.ViewService;
import org.springframework.beans.factory.annotation.Autowired;

public class ProcessRunnerMock extends ProcessRunner {
  @Autowired
  ProcessRunnerMock(ViewService viewService) {
    super(viewService);
  }

  @Override
  void startProcess(String errorLogMessage, String... command) {
    TestContext.addMessage("call [" + String.join(" ") + "]");
  }
}
