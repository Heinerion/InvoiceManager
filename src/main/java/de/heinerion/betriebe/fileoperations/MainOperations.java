/**
 * MainOperations.java
 * heiner 27.03.2012
 */
package de.heinerion.betriebe.fileoperations;

import de.heinerion.betriebe.data.System;
import de.heinerion.exceptions.HeinerionException;
import de.heinerion.betriebe.gui.ApplicationFrame;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.tools.DateUtil;
import de.heinerion.betriebe.tools.PathUtil;
import de.heinerion.latex.LatexGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static de.heinerion.betriebe.data.Constants.*;

/**
 * @author heiner
 */
public class MainOperations {
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
      File tex = writeTexFile(letter);

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
  private static void moveOutputFiles(Conveyable conveyable, File tex) {
    String title = generateTitle(conveyable);
    File parentFolder = generatePath(conveyable);

    String temp = parentFolder + File.separator + title + TEX;
    String texPath = temp.replace(PathUtil.getBaseDir() + File.separator,
        PathUtil.getSystemPath() + File.separator);
    File texDestination = new File(texPath);

    if (!texDestination.exists()) {
      try {
        texDestination.getAbsoluteFile().getParentFile().mkdirs();
        texDestination.createNewFile();
      } catch (IOException e) {
        if (logger.isErrorEnabled()) {
          logger.error("Could not create destination '" + texDestination.getAbsolutePath() + "'", e);
        }
      }
    }

    File output = new File(title + PDF);
    File destination = new File(parentFolder, title + PDF);

    tex.renameTo(texDestination);
    output.renameTo(destination);

    if (logger.isInfoEnabled()) {
      logger.info("Quelltextdatei nach {} verschoben",
          texDestination.getAbsolutePath());
      logger.info("Pdf nach {} verschoben", destination.getAbsolutePath());
    }
  }

  private static File writeTexFile(Conveyable conveyable) {
    File tex = new File(generateTitle(conveyable) + TEX);
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(tex));
      out.write(LatexGenerator.generateLatexSource(conveyable));

      out.flush();
      out.close();
    } catch (IOException e) {
      HeinerionException.handleException(MainOperations.class, e);
    }
    return tex;
  }

  private static File generatePath(Conveyable conveyable) {
    File folder;

    if (conveyable instanceof Invoice) {
      folder = conveyable.getCompany().getFolderFile();
    } else {
      folder = new File(PathUtil.determinePath(Letter.class));
    }

    if (!folder.exists()) {
      folder.mkdirs();
    }
    return folder;
  }

  private static String generateTitle(Conveyable letter) {
    String start;
    if (letter instanceof Invoice) {
      int invoiceNumber = letter.getCompany().getInvoiceNumber();
      start = invoiceNumber + "";
    } else {
      start = letter.getSubject() + " --";
    }

    String receiver = letter.getReceiver().getRecipient();
    String date = DateUtil.format(letter.getDate());

    return String.join(SPACE, start, receiver,
        date);
  }

  private static void pdfLatex(File tex) {
    // pdflatex
    String program = "pdflatex";
    String arguments = QUOTE + tex.getAbsolutePath()
        + QUOTE;
    if (logger.isInfoEnabled()) {
      logger.info("Befehl '{} {}'", program, arguments);
    }

    String[] befehl = {program, arguments,};
    ProcessBuilder pb = new ProcessBuilder(befehl);

    // IO des Prozesses auf System.out legen
    pb.inheritIO();

    try {
      Process p = pb.start();

      // Auf Prozess warten
      p.waitFor();
    } catch (IOException e) {
      if (logger.isErrorEnabled()) {
        logger.error(Translator.translate("error.pdflatex"), e);
      }
      // TODO Fehlerbehandlung über Session, diesen Abschnitt dafür verwenden
      String message = "pdflatex konnte nicht ausgeführt werden.\n"
          + "Ist das Programm installiert?";
      showExceptionDialog(e, message);
    } catch (InterruptedException e) {
      if (logger.isErrorEnabled()) {
        logger.error(e);
      }
      HeinerionException.rethrow(e);
    }
  }

  private static void deleteTempFiles(File tex) {
    String pfad = tex.getAbsolutePath();

    // Dateiendung abschneiden (inklusive .)
    String kernName = pfad.substring(0, pfad.length() - TEX.length());

    String[] endings = {"aux", "log", "out",};
    for (String ending : endings) {
      File tempfile = new File(kernName + "." + ending);

      boolean isDeleted = tempfile.delete();
      if (logger.isDebugEnabled() && isDeleted) {
        logger.debug("{}.{} gelöscht", kernName, ending);
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("{}{} erstellt, Müll beseitigt", kernName, TEX);
    }
  }

  private static void showExceptionDialog(Exception exception,
                                          String message) {
    JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), message,
        Translator.translate("error.pdflatex"), JOptionPane.ERROR_MESSAGE);
    if (System.isDebugMode()) {
      StringBuilder out = new StringBuilder();
      for (StackTraceElement ste : exception.getStackTrace()) {
        out.append(ste.getMethodName())
            .append(" : ")
            .append(ste.getFileName())
            .append(" (")
            .append(ste.getLineNumber())
            .append(")\n");
      }
      JOptionPane.showMessageDialog(ApplicationFrame.getInstance(),
          exception.getLocalizedMessage() + "\n" + out,
          Translator.translate("error.pdflatex"), JOptionPane.ERROR_MESSAGE);
    }
  }
}
