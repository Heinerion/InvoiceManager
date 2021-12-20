package de.heinerion.invoice;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.invoice.storage.loading.IO;
import de.heinerion.invoice.view.GuiStarter;
import de.heinerion.invoice.view.swing.ErrorDialog;
import de.heinerion.invoice.view.swing.LookAndFeelUtil;
import lombok.extern.flogger.Flogger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.awt.*;

/**
 * @author heiner
 */
@Flogger
@Service
public class InvoiceManager implements CommandLineRunner {
  private final GuiStarter starter;

  InvoiceManager(IO io, GuiStarter swingStarter) {
    this.starter = swingStarter;
    DataBase.getInstance().setIo(io);
  }

  public void run(String... args) {
    LookAndFeelUtil.setNimbus();
    invoke(args);
  }

  void invoke(String[] args) {
    parseArguments(args);
    setupGlobalExceptionHandling();
    start();
  }

  private void parseArguments(String... args) {
    Session.isDebugMode(false);

    for (String argument : args) {
      evaluateArgument(argument);
    }

    if (!Session.isDebugMode()) {
      log.atWarning().log("PRODUCTION MODE");
    }
  }

  public static void setupGlobalExceptionHandling() {
    Thread.setDefaultUncaughtExceptionHandler((_unused, e) -> ErrorDialog.show(e));
  }

  private void evaluateArgument(String string) {
    if ("debug".equals(string)) {
      Session.isDebugMode(true);
    }
  }

  private void start() {
    EventQueue.invokeLater(starter::showInterface);
  }
}
