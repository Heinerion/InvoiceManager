package de.heinerion.invoice.print;

import de.heinerion.invoice.models.Conveyable;
import de.heinerion.invoice.models.Invoice;
import de.heinerion.invoice.util.PathUtilNG;
import de.heinerion.invoice.view.DateUtil;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.nio.file.Path;

@Flogger
@Service
public class PrintOperations {
  private final Printer printer;
  private final PathUtilNG pathUtil;

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
    SwingUtilities.invokeLater(() -> createDocumentLater(letter));
  }

  protected void createDocumentLater(Conveyable letter) {
    String title = generateTitle(letter);
    Path folder = generatePath(letter);
    log.atInfo().log("print %s to %s", title, folder);
    printer.writeFile(letter, folder, title);
  }

  private Path generatePath(Conveyable conveyable) {
    return (conveyable instanceof Invoice)
        ? pathUtil.determineInvoicePath(conveyable.getCompany())
        : pathUtil.determineLetterPath();
  }

  private String generateTitle(Conveyable letter) {
    String start = getTitle(letter);

    String receiver = letter.getReceiver().getRecipient();
    String date = DateUtil.format(letter.getDate());

    return String.join(" ", start, receiver, date);
  }

  public String getTitle(Conveyable conveyable) {
    return (conveyable instanceof Invoice invoice)
        ? Integer.toString((invoice).getNumber())
        : conveyable.getSubject() + " --";
  }
}
