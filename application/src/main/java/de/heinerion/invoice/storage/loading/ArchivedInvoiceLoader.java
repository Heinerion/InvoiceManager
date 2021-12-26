package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.repositories.AddressRepository;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.PdfArchivedInvoice;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.XmlArchivedInvoice;
import lombok.extern.flogger.Flogger;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

@Flogger
class ArchivedInvoiceLoader extends Loader {

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
  public Loadable loopAction(final File file, AddressRepository addressRepository) {
    ArchivedInvoice data = file.getPath().endsWith("xml")
        ? new XmlArchivedInvoice(file)
        : new PdfArchivedInvoice(file, addressRepository);

    try {
      data.loadFile();
    } catch (IOException e) {
      log.atSevere().withCause(e)
          .log("the invoice file could not be read: %s", file.getAbsolutePath());
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
