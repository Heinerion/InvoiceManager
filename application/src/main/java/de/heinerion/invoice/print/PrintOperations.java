package de.heinerion.invoice.print;

import de.heinerion.invoice.models.Conveyable;
import de.heinerion.invoice.models.Invoice;
import de.heinerion.invoice.models.Letter;
import de.heinerion.invoice.util.PathUtilNG;
import de.heinerion.invoice.view.DateUtil;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Flogger
@Service
public class PrintOperations {
  private static final int STATE_LETTER = 0;
  private static final int STATE_INVOICE = 1;

  private final Printer printer;
  private final PathUtilNG pathUtil;

  private final FileInfoGenerator[] generators = {
      new LetterInfoGenerator(),
      new InvoiceInfoGenerator()};
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
   * @param letter
   *     the conveyable to be written to pdf
   */
  public void createDocument(Conveyable letter) {
    updateState(letter);

    String title = generateTitle(letter);
    Path folder = generatePath(letter);
    log.atInfo().log("print %s to %s", title, folder);
    SwingUtilities.invokeLater(() -> printer.writeFile(letter, folder, title));
  }

  private void updateState(Conveyable letter) {
    if (letter instanceof Invoice) {
      state = STATE_INVOICE;
    } else {
      state = STATE_LETTER;
    }
  }

  private Path generatePath(Conveyable conveyable) {
    Path folder = getGenerator().getFolder(conveyable);

    if (Files.exists(folder)) {
      return folder;
    }

    log.atInfo().log("create directory %s", folder);
    try {
      return Files.createDirectories(folder);
    } catch (IOException e) {
      throw new RuntimeException(
          String.format("directory %s could not be created", folder), e);
    }
  }

  private FileInfoGenerator getGenerator() {
    return generators[state];
  }

  private String generateTitle(Conveyable letter) {
    String start = getGenerator().getTitle(letter);

    String receiver = letter.getReceiver().getRecipient();
    String date = DateUtil.format(letter.getDate());

    return String.join(" ", start, receiver, date);
  }

  private interface FileInfoGenerator {
    String getTitle(Conveyable conveyable);

    Path getFolder(Conveyable conveyable);
  }

  private class LetterInfoGenerator implements FileInfoGenerator {
    @Override
    public String getTitle(Conveyable letter) {
      return ((Letter) letter).getSubject() + " --";
    }

    @Override
    public Path getFolder(Conveyable conveyable) {
      return pathUtil.determineLetterPath();
    }
  }

  private class InvoiceInfoGenerator implements FileInfoGenerator {
    @Override
    public String getTitle(Conveyable invoice) {
      return Integer.toString(((Invoice) invoice).getNumber());
    }

    @Override
    public Path getFolder(Conveyable invoice) {
      return pathUtil.determineInvoicePath(invoice.getCompany());
    }
  }
}
