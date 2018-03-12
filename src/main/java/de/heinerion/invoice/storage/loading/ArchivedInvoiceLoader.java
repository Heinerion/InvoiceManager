package de.heinerion.invoice.storage.loading;

import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.ParsingUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

class ArchivedInvoiceLoader extends AbstractLoader {
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
    return Pattern.compile(".*\\.pdf$");
  }

  @Override
  public Loadable loopAction(final File file) {
    ArchivedInvoice data = new ArchivedInvoice(file);
    try (PDDocument pdf = PDDocument.load(file)) {
      PDDocumentInformation info = pdf.getDocumentInformation();
      // Ausgelesene Informationen in den Datensatz speichern
      data.setCompany(info.getAuthor());
      data.setItem(info.getSubject());
      String keywords = info.getKeywords();
      if (keywords != null && !"".equals(keywords)) {
        data.setAmount(ParsingUtil.parseDouble(keywords));
      }
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