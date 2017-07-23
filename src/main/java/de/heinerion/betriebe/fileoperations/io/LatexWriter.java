package de.heinerion.betriebe.fileoperations.io;

import de.heinerion.betriebe.fileoperations.MainOperations;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.services.SwingService;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.services.ViewService;
import de.heinerion.betriebe.tools.PathUtil;
import de.heinerion.exceptions.HeinerionException;
import de.heinerion.latex.LatexGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.heinerion.betriebe.data.Constants.QUOTE;

public class LatexWriter {
  private Logger logger = LogManager.getLogger(LatexWriter.class);

  private static final String TEX = ".tex";
  private static final String PDF = ".pdf";

  private LatexGenerator latexGenerator = new LatexGenerator();
  private static ViewService viewService = new SwingService();

  public void writeFile(Conveyable letter, File parentFolder, String title) {
    String pathname = determineFilename(title);
    String content = latexGenerator.generateSourceContent(letter);
    File tex = writeContentToFile(pathname, content);

    pdfLatex(tex);

    moveSource(parentFolder, pathname, tex);
    moveDocument(parentFolder, title);

    removeTempFiles(pathname);

    if (logger.isDebugEnabled()) {
      logger.debug("{} created, temp files cleaned", pathname);
    }
  }

  private String determineFilename(String title) {
    return title + TEX;
  }

  private File writeContentToFile(String pathname, String content) {
    File target = new File(pathname);
    try (BufferedWriter out = new BufferedWriter(new FileWriter(target))) {
      out.write(content);

      out.flush();
      out.close();
    } catch (IOException e) {
      HeinerionException.handleException(MainOperations.class, e);
    }
    return target;
  }

  private void pdfLatex(File tex) {
    String[] command = prepareCommand(tex);
    ProcessBuilder pb = prepareProcessBuilder(command);

    try {
      runProcess(pb);
    } catch (IOException e) {
      if (logger.isErrorEnabled()) {
        logger.error(Translator.translate("error.pdflatex"), e);
      }

      String program = command[0];
      String message = "command could not be executed.\n" + "Is " + program + " installed?";
      viewService.showExceptionMessage(e, message);
    } catch (InterruptedException e) {
      if (logger.isErrorEnabled()) {
        logger.error(e);
      }

      Thread.currentThread().interrupt();
    }
  }

  private String[] prepareCommand(File tex) {
    String program = "pdflatex";
    String arguments = QUOTE + tex.getAbsolutePath() + QUOTE;
    if (logger.isInfoEnabled()) {
      logger.info("command '{} {}'", program, arguments);
    }

    return new String[]{program, arguments};
  }

  private ProcessBuilder prepareProcessBuilder(String[] command) {
    ProcessBuilder pb = new ProcessBuilder(command);

    // map process IO to stdin / stdout
    pb.inheritIO();

    return pb;
  }

  private void runProcess(ProcessBuilder pb) throws IOException, InterruptedException {
    Process p = pb.start();

    p.waitFor();
  }

  private void moveSource(File parentFolder, String pathname, File tex) {
    move(parentFolder, pathname, tex);
  }

  private void move(File parentFolder, String pdfPathname, File output) {
    String oldRoot = PathUtil.getBaseDir() + File.separator;
    String destinationRoot = PathUtil.getSystemPath() + File.separator;

    File destination = prepareDestinationFile(parentFolder, pdfPathname, oldRoot, destinationRoot);
    boolean success = output.renameTo(destination);

    if (logger.isInfoEnabled()) {
      logger.info("File {} moved to {}", success ? "was" : "could not be", destination.getAbsolutePath());
    }
  }

  private File prepareDestinationFile(File parentFolder, String pathname, String oldRoot, String destinationRoot) {
    String temp = parentFolder + File.separator + pathname;

    String texPath = temp.replace(oldRoot, destinationRoot);
    File texDestination = new File(texPath);

    if (!texDestination.exists()) {
      createDestinationFile(texDestination);
    }

    return texDestination;
  }

  private void moveDocument(File parentFolder, String title) {
    String pdfPathname = title + PDF;
    File output = new File(pdfPathname);
    move(parentFolder, pdfPathname, output);
  }

  private void createDestinationFile(File texDestination) {
    try {
      boolean dirsCreated = texDestination
          .getAbsoluteFile()
          .getParentFile()
          .mkdirs();

      boolean fileCreated = texDestination.createNewFile();

      if (logger.isInfoEnabled()) {
        List<String> messages = new ArrayList<>();
        if (fileCreated) {
          messages.add("File '" + texDestination.getAbsolutePath() + "' created.");
        }
        if (dirsCreated) {
          messages.add("Directories created.");
        }

        logger.info(Strings.join(messages, ' '));
      }
    } catch (IOException e) {
      if (logger.isErrorEnabled()) {
        logger.error("Could not create destination '" + texDestination.getAbsolutePath() + "'", e);
      }
    }
  }

  private void removeTempFiles(String pathname) {
    String[] endings = {"aux", "log", "out",};
    for (String ending : endings) {
      File tempFile = new File(pathname.replace("tex", ending));

      if (tempFile.delete()) {
        if (logger.isDebugEnabled()) {
          logger.debug("{}.{} removed", pathname, ending);
        }
      } else {
        if (logger.isWarnEnabled()) {
          logger.warn("{}.{} could not be removed", pathname, ending);
        }
      }
    }
  }
}
