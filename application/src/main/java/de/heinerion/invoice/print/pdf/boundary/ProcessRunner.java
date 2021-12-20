package de.heinerion.invoice.print.pdf.boundary;

import de.heinerion.betriebe.data.Session;
import de.heinerion.invoice.Translator;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.IOException;

@Flogger
@Service
class ProcessRunner {
  String quote(String string) {
    return "\"" + string + "\"";
  }

  void startProcess(String errorLogMessage, String... command) {
    ProcessBuilder pb = new ProcessBuilder(command);
    // map process IO to stdin / stdout
    pb.inheritIO();

    try {
      Process p = pb.start();
      p.waitFor();
    } catch (IOException e) {
      log.atSevere().withCause(e).log(errorLogMessage);

      String program = command[0];
      String message = "command could not be executed.\n" + "Is " + program + " installed?";
      showExceptionMessage(e, message);
    } catch (InterruptedException e) {
      log.atSevere().withCause(e).log(errorLogMessage);

      Thread.currentThread().interrupt();
    }
  }

  private void showExceptionMessage(Exception exception, String message) {
    JOptionPane.showMessageDialog(null, message,
        Translator.translate("error.pdflatex"), JOptionPane.ERROR_MESSAGE);
    if (Session.isDebugMode()) {
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
