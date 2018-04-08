package de.heinerion.invoice;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.invoice.aspects.annotations.LogBefore;
import de.heinerion.invoice.aspects.annotations.LogMethod;
import de.heinerion.invoice.storage.loading.IO;
import de.heinerion.invoice.view.GuiStarter;
import de.heinerion.invoice.view.swing.LookAndFeelUtil;
import de.heinerion.invoice.view.swing.SwingStarter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author heiner
 */
final class InvoiceManager {
  private static final Logger LOGGER = LogManager.getLogger(InvoiceManager.class);

  private SwingStarter swingStarter;
  private GuiStarter starter;

  @Autowired
  InvoiceManager(IO io, SwingStarter swingStarter) {
    this.swingStarter = swingStarter;
    this.starter = swingStarter;
    DataBase.setIo(io);
  }

  @LogMethod
  public static void main(String... args) {
    LookAndFeelUtil.setNimbus();
    InvoiceManager application = ConfigurationService.getBean(InvoiceManager.class);

    application.invoke(args);
  }

  void invoke(String[] args) {
    parseArguments(args);
    start();
  }

  private void parseArguments(String... args) {
    Session.isDebugMode(false);

    for (String argument : args) {
      evaluateArgument(argument);
    }

    if (!Session.isDebugMode()) {
      LOGGER.warn("PRODUCTION MODE");
    }
  }

  private void evaluateArgument(String string) {
    switch (string) {
      case "debug":
        Session.isDebugMode(true);
        break;
      case "swing":
        starter = swingStarter;
        break;
      default:
        break;
    }
  }

  @LogBefore
  private void start() {
    starter.showInterface();
  }
}
