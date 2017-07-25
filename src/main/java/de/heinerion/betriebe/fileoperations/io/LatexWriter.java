package de.heinerion.betriebe.fileoperations.io;

import de.heinerion.betriebe.fileoperations.MainOperations;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.services.ViewService;
import de.heinerion.betriebe.tools.PathUtil;
import de.heinerion.exceptions.HeinerionException;
import de.heinerion.latex.LatexGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

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

  @Autowired
  private LatexGenerator latexGenerator;

  @Autowired
  private ViewService viewService;

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
    String temp = combineAbsolutePath(parentFolder, pathname);

    String destinationPath = changePathFromBaseToSystem(temp);

    moveFile(destinationPath, tex);
  }

  private String changePathFromBaseToSystem(String temp) {
    String oldRoot = PathUtil.getBaseDir() + File.separator;
    String destinationRoot = PathUtil.getSystemPath() + File.separator;

    return temp.replace(oldRoot, destinationRoot);
  }

  private String combineAbsolutePath(File parentFolder, String pathname) {
    return parentFolder + File.separator + pathname;
  }

  private void moveFile(String destinationPath, File output) {
    File destination = prepareFile(destinationPath);
    boolean success = output.renameTo(destination);

    if (success) {
      if (logger.isInfoEnabled()) {
        logger.info("File moved to {}", destination.getAbsolutePath());
      }
    } else {
      if (logger.isWarnEnabled()) {
        logger.warn("File could not be moved to {}", destination.getAbsolutePath());
      }
    }
  }

  private File prepareFile(String texPath) {
    File texDestination = new File(texPath);

    if (!texDestination.exists()) {
      createDestinationFile(texDestination);
    }

    return texDestination;
  }

  private void moveDocument(File parentFolder, String title) {
    String pdfPathname = title + PDF;
    String destinationPath = combineAbsolutePath(parentFolder, pdfPathname);

    File output = new File(pdfPathname);
    moveFile(destinationPath, output);
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
