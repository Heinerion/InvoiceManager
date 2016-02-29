/**
 * MainOperations.java
 * heiner 27.03.2012
 */
package de.heinerion.betriebe.classes.fileOperations;

import de.heinerion.betriebe.classes.gui.RechnungFrame;
import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.enums.Utilities;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.tools.DateTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author heiner
 */
public final class MainOperations {
  private static final Logger logger = LogManager.getLogger(MainOperations.class);

  private static final String TEX = ".tex";
  private static final String PDF = ".pdf";

  private MainOperations() {
  }

  /**
   * Erstellt das Dokument auf der Festplatte.
   * <p>
   * <ul>
   * <li>Legt die Tex-Quelle an
   * <li>generiert den Latex Quelltext
   * <li>führt pdfLatex aus
   * <li>verschieb Ergebnis-Pdf
   * <li>löscht Hilfsdateien
   * </ul>
   *
   * @param letter Das Dokument das erstellt werden soll
   */
  public static void createDocument(Conveyable letter) {
    SwingUtilities.invokeLater(() -> {
      final File tex = writeTexFile(letter);

      pdfLatex(tex);

      moveOutputFiles(letter, tex);

      deleteTempFiles(tex);

      IO.load();
    });
  }

  /**
   * Verschiebt die Quelldatei (.tex) und die Ergebnisdatei (.pdf).
   * <p>
   * Der Zeilordner der jeweiligen Datei wird aus dem Conveyable abgeleitet.
   *
   * @param conveyable Das ursprüngliche Dokument
   * @param tex        Die Tex-Datei
   */
  private static void moveOutputFiles(Conveyable conveyable, final File tex) {
    final String title = generateTitle(conveyable);
    final File parentFolder = generatePath(conveyable);

    final String temp = parentFolder + File.separator + title + TEX;
    final String texPath = temp.replace(Utilities.applicationHome(),
        Utilities.SYSTEM.toString() + File.separator);
    final File texDestination = new File(texPath);

    if (!texDestination.exists()) {
      try {
        texDestination.getAbsoluteFile().getParentFile().mkdirs();
        texDestination.createNewFile();
      } catch (final IOException e) {
        logger.error("Could not create destination '" + texDestination.getAbsolutePath() + "'", e);
      }
    }

    final File output = new File(title + PDF);
    final File destination = new File(parentFolder, title + PDF);

    tex.renameTo(texDestination);
    output.renameTo(destination);

    if (logger.isDebugEnabled()) {
      logger.debug("Quelltextdatei nach {} verschoben",
          texDestination.getAbsolutePath());
      logger.debug("Pdf nach {} verschoben", destination.getAbsolutePath());
    }
  }

  private static File writeTexFile(Conveyable conveyable) {
    final File tex = new File(generateTitle(conveyable) + TEX);
    try {
      final BufferedWriter out = new BufferedWriter(new FileWriter(tex));
      out.write(LatexGenerator.generateLatexSource(conveyable));

      out.flush();
      out.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return tex;
  }

  private static File generatePath(Conveyable conveyable) {
    File ordner;

    if (conveyable instanceof Invoice) {
      ordner = conveyable.getCompany().getPfad();
    } else {
      ordner = new File(Utilities.BRIEF.getPath());
    }

    if (!ordner.exists()) {
      ordner.mkdirs();
    }
    return ordner;
  }

  private static String generateTitle(Conveyable letter) {
    final String start;
    if (letter instanceof Invoice) {
      final int invoiceNumber = letter.getCompany().getInvoiceNumber();
      start = invoiceNumber + "";
    } else {
      start = letter.getSubject() + " --";
    }

    final String receiver = letter.getReceiver().getRecipient();
    final String date = DateTools.format(letter.getDate());

    return String.join(Constants.SPACE, start, receiver,
        date);
  }

  private static void pdfLatex(File tex) {
    // pdflatex
    final String program = "pdflatex";
    final String arguments = Constants.QUOTE + tex.getAbsolutePath()
        + Constants.QUOTE;
    logger.info("Befehl '{} {}'", program, arguments);

    final String[] befehl = {program, arguments,};
    final ProcessBuilder pb = new ProcessBuilder(befehl);

    // IO des Prozesses auf System.out legen
    pb.inheritIO();

    try {
      final Process p = pb.start();

      // Auf Prozess warten
      p.waitFor();
    } catch (final IOException e) {
      logger.error(Constants.ERROR_PDFLATEX, e);
      // TODO Fehlerbehandlung über Session, diesen Abschnitt dafür verwenden
      final String message = "pdflatex konnte nicht ausgeführt werden.\n"
          + "Ist das Programm installiert?";
      showExceptionDialog(e, message);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void deleteTempFiles(File tex) {
    final String pfad = tex.getAbsolutePath();

    // Dateiendung abschneiden (inklusive .)
    final String kernName = pfad.substring(0, pfad.length() - TEX.length());

    final String[] endings = {"aux", "log", "out",};
    for (String ending : endings) {
      final File tempfile = new File(kernName + "." + ending);

      final boolean isDeleted = tempfile.delete();
      if (logger.isDebugEnabled() && isDeleted) {
        logger.debug("{}.{} gelöscht", kernName, ending);
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("{}{} erstellt, Müll beseitigt", kernName, TEX);
    }
  }

  private static void showExceptionDialog(final Exception exception,
                                          String message) {
    JOptionPane.showMessageDialog(RechnungFrame.getInstance(), message,
        Constants.ERROR_PDFLATEX, JOptionPane.ERROR_MESSAGE);
    if (Utilities.isDebugMode()) {
      String out = "";
      for (final StackTraceElement ste : exception.getStackTrace()) {
        out += ste.getMethodName() + " : " + ste.getFileName() + " ("
            + ste.getLineNumber() + ")\n";
      }
      JOptionPane.showMessageDialog(RechnungFrame.getInstance(),
          exception.getLocalizedMessage() + "\n" + out,
          Constants.ERROR_PDFLATEX, JOptionPane.ERROR_MESSAGE);
    }
  }
}
