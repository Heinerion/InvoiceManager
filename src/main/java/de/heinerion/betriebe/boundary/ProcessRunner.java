package de.heinerion.betriebe.boundary;

import de.heinerion.betriebe.services.ViewService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static de.heinerion.betriebe.data.Constants.QUOTE;

class ProcessRunner {
  private Logger logger = LogManager.getLogger(ProcessRunner.class);

  private final ViewService viewService;

  @Autowired
  ProcessRunner(ViewService viewService) {
    this.viewService = viewService;
  }

  String quote(String string) {
    return QUOTE + string + QUOTE;
  }

  void startProcess(String errorLogMessage, String... command) {
    ProcessBuilder pb = new ProcessBuilder(command);
    // map process IO to stdin / stdout
    pb.inheritIO();

    try {
      Process p = pb.start();
      p.waitFor();
    } catch (IOException e) {
      logException(e, errorLogMessage);

      String program = command[0];
      String message = "command could not be executed.\n" + "Is " + program + " installed?";
      viewService.showExceptionMessage(e, message);
    } catch (InterruptedException e) {
      logException(e, errorLogMessage);

      Thread.currentThread().interrupt();
    }
  }

  private void logException(Exception e, String errorLogMessage) {
    if (logger.isErrorEnabled()) {
      logger.error(errorLogMessage, e);
    }
  }
}
