package de.heinerion.betriebe.classes.file_operations.loading;

import de.heinerion.betriebe.classes.data.RechnungData;
import de.heinerion.betriebe.exceptions.HeinerionException;
import de.heinerion.betriebe.tools.ParsingUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public final class RechnungDataLoader extends AbstractLoader<RechnungData> {
  private static final Logger logger = LogManager.getLogger(RechnungDataLoader.class);

  public RechnungDataLoader(File aLoadDirectory) {
    super(aLoadDirectory);
  }

  @Override
  public String getDescriptiveName() {
    return "Rechnungen";
  }

  @Override
  protected Pattern getPattern() {
    return Pattern.compile(".*\\.pdf$");
  }

  @Override
  public Loadable loopAction(final File file) {
    RechnungData data = new RechnungData(file);
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
