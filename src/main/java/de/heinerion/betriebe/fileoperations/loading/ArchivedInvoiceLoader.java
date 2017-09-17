package de.heinerion.betriebe.fileoperations.loading;

import de.heinerion.betriebe.gui.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.exceptions.HeinerionException;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.util.ParsingUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public final class ArchivedInvoiceLoader extends AbstractLoader<ArchivedInvoice> {
  private static final Logger logger = LogManager.getLogger(ArchivedInvoiceLoader.class);

  public ArchivedInvoiceLoader(File aLoadDirectory) {
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
      HeinerionException.rethrow(e);
    }

    return data;
  }
}
