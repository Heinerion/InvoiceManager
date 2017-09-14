/**
 * MainOperations.java
 * heiner 27.03.2012
 */
package de.heinerion.betriebe.fileoperations;

import de.heinerion.betriebe.fileoperations.io.LatexWriter;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.util.DateUtil;
import de.heinerion.betriebe.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.io.File;

import static de.heinerion.betriebe.util.Constants.SPACE;

/**
 * @author heiner
 */
public class MainOperations {
  private LatexWriter latexWriter;

  private static final int STATE_LETTER = 0;
  private static final int STATE_INVOICE = 1;

  private FileInfoGenerator[] generators = {new LetterInfoGenerator(), new InvoiceInfoGenerator()};
  private int state = STATE_LETTER;

  @Autowired
  public MainOperations(LatexWriter latexWriter) {
    this.latexWriter = latexWriter;
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
  public void createDocument(Letter letter) {
    updateState(letter);

    SwingUtilities.invokeLater(() -> {
      latexWriter.writeFile(letter, generatePath(letter), generateTitle(letter));

      IO.load();
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

    return String.join(SPACE, start, receiver, date);
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
      return new File(PathUtil.determinePath(Letter.class));
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
      return conveyable.getCompany().getFolderFile();
    }
  }
}
