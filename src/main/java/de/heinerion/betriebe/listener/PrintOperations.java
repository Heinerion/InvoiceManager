package de.heinerion.betriebe.listener;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.view.DateUtil;

import javax.swing.*;
import java.io.File;

/**
 * @author heiner
 */
class PrintOperations {
  private static final int STATE_LETTER = 0;
  private static final int STATE_INVOICE = 1;

  private DataBase dataBase = DataBase.getInstance();
  private Printer printer;
  private PathUtilNG pathUtil;

  private FileInfoGenerator[] generators = {new LetterInfoGenerator(), new InvoiceInfoGenerator()};
  private int state = STATE_LETTER;

  PrintOperations(Printer printer, PathUtilNG pathUtil) {
    this.printer = printer;
    this.pathUtil = pathUtil;
  }

  /**
   * Writes the conveyable to pdf
   * <ul>
   * <li>creates .tex source file
   * <li>generates latex source code from conveyable
   * <li>runs pdflatex on the created source code
   * <li>moves the source code and the pdf to their destinations
   * <li>removes auxiliary files created in the process
   * </ul>
   *
   * @param letter the conveyable to be written to pdf
   */
  void createDocument(Letter letter) {
    updateState(letter);

    SwingUtilities.invokeLater(() -> {
      printer.writeFile(letter, generatePath(letter), generateTitle(letter));

      dataBase.load();
    });
  }

  private void updateState(Letter letter) {
    if (letter instanceof Invoice) {
      state = STATE_INVOICE;
    } else {
      state = STATE_LETTER;
    }
  }

  private File generatePath(Letter conveyable) {
    File folder = getGenerator().getFolder(conveyable);

    if (!folder.exists()) {
      folder.mkdirs();
    }

    return folder;
  }

  private FileInfoGenerator getGenerator() {
    return generators[state];
  }

  private String generateTitle(Letter letter) {
    String start = getGenerator().getTitle(letter);

    String receiver = letter.getReceiver().getRecipient();
    String date = DateUtil.format(letter.getDate());

    return String.join(" ", start, receiver, date);
  }

  private interface FileInfoGenerator {
    String getTitle(Letter letter);

    File getFolder(Letter conveyable);
  }

  private class LetterInfoGenerator implements FileInfoGenerator {
    @Override
    public String getTitle(Letter letter) {
      return letter.getSubject() + " --";
    }

    @Override
    public File getFolder(Letter conveyable) {
      return new File(pathUtil.determinePath(Letter.class));
    }
  }

  private class InvoiceInfoGenerator implements FileInfoGenerator {
    @Override
    public String getTitle(Letter letter) {
      int invoiceNumber = letter.getCompany().getInvoiceNumber();
      return Integer.toString(invoiceNumber);
    }

    @Override
    public File getFolder(Letter conveyable) {
      return conveyable.getCompany().getFolderFile(pathUtil.determinePath(conveyable.getClass()));
    }
  }
}
