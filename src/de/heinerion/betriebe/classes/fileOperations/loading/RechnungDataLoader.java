package de.heinerion.betriebe.classes.fileOperations.loading;

import de.heinerion.betriebe.classes.data.RechnungData;
import de.heinerion.betriebe.tools.ParsingTools;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public final class RechnungDataLoader extends AbstractLoader<RechnungData> {

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
    final RechnungData data = new RechnungData(file);
    try {
      final PDDocument pdf = PDDocument.load(file);
      final PDDocumentInformation info = pdf.getDocumentInformation();
      // Ausgelesene Informationen in den Datensatz speichern
      data.setCompany(info.getAuthor());
      data.setItem(info.getSubject());
      final String keywords = info.getKeywords();
      if (keywords != null && !"".equals(keywords)) {
        data.setAmount(ParsingTools.parseDouble(keywords));
      }
      pdf.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return data;
  }
}
