package de.heinerion.invoice.boundary;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Flogger
@Service
@RequiredArgsConstructor
public class ProcessRunner {
  private final PathUtilNG pathUtil;
  private final Session session = Session.getInstance();

  String quote(String string) {
    return "\"" + string + "\"";
  }

  public void startProcess(String errorLogMessage, String... command) {
    String program = command[0];

    ProcessBuilder pb = new ProcessBuilder(command);
    pb.directory(pathUtil.getWorkingDirectory().toFile());
    // map process IO to stdin / stdout
    pb.inheritIO();
    Path logFile = createLogFile(program);
    pb.redirectOutput(logFile.toFile());
    log.atInfo().log("writing output to %s", logFile);

    try {
      Process p = pb.start();
      boolean completedInTime = p.waitFor(10, TimeUnit.SECONDS);
      if (!completedInTime) {
        log.atWarning().log("process '%s' did not complete in time", program);
        throw new RuntimeException("process '%s' did not complete in time".formatted(program));
      }
    } catch (IOException e) {
      log.atSevere().withCause(e).log(errorLogMessage);

      String message = "command could not be executed.\n" + "Is " + program + " installed?";
      showExceptionMessage(e, message);
    } catch (InterruptedException e) {
      log.atSevere().withCause(e).log(errorLogMessage);

      Thread.currentThread().interrupt();
    }
  }

  private Path createLogFile(String program) {
    var timestamp = ZonedDateTime
        .now(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("uuuu-MM-dd HHmmssSSS"));

    var logFile = pathUtil.getLogPath(program).resolve(timestamp + ".log");
    try {
      Files.createFile(logFile);
    } catch (IOException e) {
      throw new RuntimeException(logFile + " could not be created", e);
    }

    return logFile;
  }

  private void showExceptionMessage(Exception exception, String message) {
    JOptionPane.showMessageDialog(null, message,
        Translator.translate("error.pdflatex"), JOptionPane.ERROR_MESSAGE);
    if (session.isDebugMode()) {
      StringBuilder out = new StringBuilder();
      for (StackTraceElement ste : exception.getStackTrace()) {
        out.append(ste.getMethodName())
            .append(" : ")
            .append(ste.getFileName())
            .append(" (")
            .append(ste.getLineNumber())
            .append(")\n");
      }
      JOptionPane.showMessageDialog(null,
          exception.getLocalizedMessage() + "\n" + out,
          Translator.translate("error.pdflatex"), JOptionPane.ERROR_MESSAGE);
    }
  }

}
