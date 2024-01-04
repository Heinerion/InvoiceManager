package de.heinerion.invoice;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.view.GuiStarter;
import de.heinerion.invoice.view.swing.ErrorDialog;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.awt.*;

/**
 * @author heiner
 */
@Flogger
@Service
@RequiredArgsConstructor
@Profile("Prod")
public class InvoiceManager implements CommandLineRunner {
  private final Session session;
  private final GuiStarter starter;
  
  public void run(String... args) {
    invoke(args);
  }

  void invoke(String[] args) {
    parseArguments(args);
    setupGlobalExceptionHandling();
    start();
  }

  private void parseArguments(String... args) {
    session.isDebugMode(false);

    for (String argument : args) {
      evaluateArgument(argument);
    }

    if (!session.isDebugMode()) {
      log.atWarning().log("PRODUCTION MODE");
    }
  }

  public void setupGlobalExceptionHandling() {
    Thread.setDefaultUncaughtExceptionHandler((ignored, e) -> new ErrorDialog(session).show(e));
  }

  private void evaluateArgument(String string) {
    if ("debug".equals(string)) {
      session.isDebugMode(true);
    }
  }

  private void start() {
    EventQueue.invokeLater(starter::showInterface);
  }
}
