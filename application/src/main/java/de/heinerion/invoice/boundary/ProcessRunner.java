package de.heinerion.invoice.boundary;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Flogger
@Service
@RequiredArgsConstructor
public class ProcessRunner {
  private final PathUtilNG pathUtil;
  private final Session session;
  private final HostSystem hostSystem;

  public static String quote(String string) {
    return "\"" + string + "\"";
  }

  public boolean startProcess(String errorLogMessage, String... command) {
    ProcessBuilder builder = new ProcessBuilder(command);
    return startProcess(builder, errorLogMessage, Duration.of(10, ChronoUnit.SECONDS), command);
  }

  boolean startProcess(ProcessBuilder pb, String errorLogMessage, Duration maxWait, String... command) {
    String program = determineProgramName(command);

    setWorkingDirectory(pb);
    redirectProcessIO(pb, createLogFile(program));

    try {
      runProcess(pb, maxWait, program);
      return true;
    } catch (IOException e) {
      showError(errorLogMessage, e, program);
      return false;
    } catch (InterruptedException e) {
      interrupt(errorLogMessage, e);
      return false;
    }
  }

  private static String determineProgramName(String[] command) {
    return command[0];
  }

  private void setWorkingDirectory(ProcessBuilder pb) {
    pb.directory(pathUtil.getWorkingDirectory().toFile());
  }

  private void redirectProcessIO(ProcessBuilder pb, Path logFile) {
    pb.inheritIO();
    pb.redirectOutput(logFile.toFile());
    pb.redirectError(logFile.toFile());
    log.atInfo().log("writing output to %s", logFile);
  }

  private static void runProcess(ProcessBuilder pb, Duration maxWait, String program) throws IOException, InterruptedException {
    Process p = startProcess(pb);
    if (!hasCompleted(p, maxWait)) {
      log.atWarning().log("process '%s' did not complete in time", program);
      throw new RuntimeException("process '%s' did not complete in time".formatted(program));
    }
  }

  private static Process startProcess(ProcessBuilder pb) throws IOException {
    return pb.start();
  }

  private static boolean hasCompleted(Process p, Duration maxWait) throws InterruptedException {
    return p.waitFor(maxWait.get(ChronoUnit.SECONDS), TimeUnit.SECONDS);
  }

  private void showError(String errorLogMessage, IOException e, String program) {
    log.atSevere().withCause(e).log(errorLogMessage);

    String message = "command could not be executed.\n" + "Is " + program + " installed?";
    showExceptionMessage(e, message);
  }

  private static void interrupt(String errorLogMessage, InterruptedException e) {
    log.atSevere().withCause(e).log(errorLogMessage);

    Thread.currentThread().interrupt();
  }

  private Path createLogFile(String program) {
    var timestamp = ZonedDateTime
        .now(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("uuuu-MM-dd HHmmssSSS"));

    var logFile = pathUtil.getLogPath(program).resolve(timestamp + ".log");
    if (hostSystem.createFile(logFile)) {
      log.atFine().log("create logfile at %s", logFile);
    } else {
      throw new RuntimeException(logFile + " could not be created");
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
