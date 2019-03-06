package de.heinerion.invoice.storage.loading;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.PdfArchivedInvoice;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.XmlArchivedInvoice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

class ArchivedInvoiceLoader extends Loader {
  private static final Logger logger = LogManager.getLogger(ArchivedInvoiceLoader.class);

  ArchivedInvoiceLoader(File aLoadDirectory) {
    super(aLoadDirectory);
  }

  @Override
  public String getDescriptiveName() {
    return Translator.translate("invoice.title");
  }

  @Override
  protected Pattern getPattern() {
    return Pattern.compile("(.*\\.pdf$)|(.*\\.xml$)");
  }

  @Override
  public Loadable loopAction(final File file) {
    ArchivedInvoice data;
    if (file.getPath().endsWith("xml"))
      data = new XmlArchivedInvoice(file);
    else
      data = new PdfArchivedInvoice(file);

    try {
      data.loadFile();
    } catch (IOException e) {
      if (logger.isErrorEnabled()) {
        logger.error(e);
      }
      throw new LoadArchivedInvoiceException(file, e);
    }

    return data;
  }

  private static class LoadArchivedInvoiceException extends RuntimeException {
    LoadArchivedInvoiceException(File causingFile, Throwable cause) {
      super(generateMessage(causingFile), cause);
    }

    private static String generateMessage(File file) {
      return "Invoice '" + file.getName() + "' caused Problems\nPath: " + file.getAbsolutePath();
    }
  }
}
