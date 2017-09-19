package de.heinerion.betriebe.listener;

import de.heinerion.betriebe.boundary.HostSystem;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.util.PathUtil;
import de.heinerion.betriebe.view.latex.LatexGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

class LatexWriter {
  private Logger logger = LogManager.getLogger(LatexWriter.class);

  private static final String TEX = ".tex";
  private static final String PDF = ".pdf";

  private final HostSystem hostSystem;
  private final LatexGenerator latexGenerator;

  @Autowired
  LatexWriter(LatexGenerator latexGenerator, HostSystem hostSystem) {
    this.latexGenerator = latexGenerator;
    this.hostSystem = hostSystem;
  }

  void writeFile(Letter letter, File parentFolder, String title) {
    String pathname = determineFilename(title);
    String content = latexGenerator.generateSourceContent(letter);
    File tex = hostSystem.writeToFile(pathname, content);

    hostSystem.pdfLatex(tex);

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

  private void moveSource(File parentFolder, String pathname, File tex) {
    String temp = combineAbsolutePath(parentFolder, pathname);

    String destinationPath = changePathFromBaseToSystem(temp);

    hostSystem.moveFile(destinationPath, tex);
  }

  private String changePathFromBaseToSystem(String temp) {
    String oldRoot = PathUtil.getBaseDir() + File.separator;
    String destinationRoot = PathUtil.getSystemPath() + File.separator;

    return temp.replace(oldRoot, destinationRoot);
  }

  private String combineAbsolutePath(File parentFolder, String pathname) {
    return parentFolder + File.separator + pathname;
  }

  private void moveDocument(File parentFolder, String title) {
    String pdfPathname = title + PDF;
    String destinationPath = combineAbsolutePath(parentFolder, pdfPathname);

    File output = new File(pdfPathname);
    hostSystem.moveFile(destinationPath, output);
  }

  private void removeTempFiles(String pathname) {
    String[] endings = {"aux", "log", "out"};
    for (String ending : endings) {
      String filename = pathname.replace("tex", ending);
      hostSystem.deleteFile(filename);
    }
  }
}
