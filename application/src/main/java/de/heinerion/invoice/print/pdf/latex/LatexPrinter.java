package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.invoice.models.Letter;
import de.heinerion.invoice.util.PathUtilNG;
import de.heinerion.invoice.print.Printer;
import de.heinerion.invoice.print.pdf.boundary.HostSystem;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.File;

@Flogger
@Service("Latex")
public class LatexPrinter implements Printer {
  private static final String TEX = ".tex";
  private static final String PDF = ".pdf";

  private final HostSystem hostSystem;
  private final LatexGenerator latexGenerator;
  private final PathUtilNG pathUtil;

  LatexPrinter(HostSystem hostSystem, LatexGenerator latexGenerator, PathUtilNG pathUtil) {
    this.hostSystem = hostSystem;
    this.latexGenerator = latexGenerator;
    this.pathUtil = pathUtil;
  }

  @Override
  public void writeFile(Letter letter, File parentFolder, String title) {
    String pathname = determineFilename(title);
    String content = latexGenerator.generateSourceContent(letter);
    File tex = hostSystem.writeToFile(pathname, content);

    hostSystem.pdfLatex(tex);

    moveSource(parentFolder, pathname, tex);
    moveDocument(parentFolder, title);

    removeTempFiles(pathname);

    log.atFine().log("%s created, temp files cleaned", pathname);
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
    String oldRoot = pathUtil.getBaseDir() + File.separator;
    String destinationRoot = pathUtil.getSystemPath() + File.separator;

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
