/**
 * MainOperations.java
 * heiner 27.03.2012
 */
package de.heinerion.betriebe.fileoperations;

import de.heinerion.betriebe.fileoperations.io.LatexWriter;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.tools.DateUtil;
import de.heinerion.betriebe.tools.PathUtil;

import javax.swing.*;
import java.io.File;

import static de.heinerion.betriebe.data.Constants.SPACE;

/**
 * @author heiner
 */
public class MainOperations {
  private static LatexWriter latexWriter = new LatexWriter();

  private static final int STATE_LETTER = 0;
  private static final int STATE_INVOICE = 1;

  private FileInfoGenerator[] generators = {new LetterInfoGenerator(), new InvoiceInfoGenerator()};
  private int state = STATE_LETTER;

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
  public void createDocument(Conveyable letter) {
    updateState(letter);

    SwingUtilities.invokeLater(() -> {
      latexWriter.writeFile(letter, generatePath(letter), generateTitle(letter));

      IO.load();
    });
  }

  private void updateState(Conveyable letter) {
    if (letter instanceof Invoice) {
      state = STATE_INVOICE;
    } else {
      state = STATE_LETTER;
    }
  }

  private File generatePath(Conveyable conveyable) {
    File folder = getGenerator().getFolder(conveyable);

    if (!folder.exists()) {
      folder.mkdirs();
    }

    return folder;
  }

  private FileInfoGenerator getGenerator() {
    return generators[state];
  }

  private String generateTitle(Conveyable letter) {
    String start = getGenerator().getTitle(letter);

    String receiver = letter.getReceiver().getRecipient();
    String date = DateUtil.format(letter.getDate());

    return String.join(SPACE, start, receiver, date);
  }

  private interface FileInfoGenerator {
    String getTitle(Conveyable letter);

    File getFolder(Conveyable conveyable);
  }

  private class LetterInfoGenerator implements FileInfoGenerator {
    @Override
    public String getTitle(Conveyable letter) {
      return letter.getSubject() + " --";
    }

    @Override
    public File getFolder(Conveyable conveyable) {
      return new File(PathUtil.determinePath(Letter.class));
    }
  }

  private class InvoiceInfoGenerator implements FileInfoGenerator {
    @Override
    public String getTitle(Conveyable letter) {
      int invoiceNumber = letter.getCompany().getInvoiceNumber();
      return Integer.toString(invoiceNumber);
    }

    @Override
    public File getFolder(Conveyable conveyable) {
      return conveyable.getCompany().getFolderFile();
    }
  }
}
