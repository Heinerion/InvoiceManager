package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.invoice.boundary.HostSystem;
import de.heinerion.invoice.models.Letter;
import de.heinerion.invoice.print.Printer;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Flogger
@Service("Latex")
@RequiredArgsConstructor
public class LatexPrinter implements Printer {
  private static final String TEX = ".tex";
  private static final String PDF = ".pdf";

  private final HostSystem hostSystem;
  private final LatexGenerator latexGenerator;
  private final PathUtilNG pathUtil;

  @Override
  public void writeFile(Letter letter, File targetFolder, String title) {
    Path workingDirectory = pathUtil.getWorkingDirectory();
    String texName = title + TEX;

    Path sourceFile = workingDirectory.resolve(texName);
    String content = latexGenerator.generateSourceContent(letter);
    hostSystem.writeToFile(sourceFile, content);

    hostSystem.pdfLatex(sourceFile.toFile());

    moveSource(targetFolder, workingDirectory.resolve(title + TEX));
    moveSource(targetFolder, workingDirectory.resolve(title + PDF));

    removeTempFiles(workingDirectory, title);

    log.atFine().log("%s created, temp files cleaned", sourceFile);
  }

  private void moveSource(File targetFolder, Path sourceFile) {
    Path target = Path.of(targetFolder.getAbsolutePath()).resolve(sourceFile.getFileName());
    try {
      log.atInfo().log("Move from %s to %s", sourceFile, target);
      Files.move(sourceFile, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    } catch (IOException ioe) {
      log.atWarning()
          .withCause(ioe)
          .log("File could not be moved to %s.", target);
    }
  }

  private void removeTempFiles(Path directory, String title) {
    String[] endings = {".aux", ".log", ".out"};
    for (String ending : endings) {
      Path filename = directory.resolve(title + ending);
      try {
        Files.delete(filename);
      } catch (IOException e) {
        log.atWarning().log("%s could not be removed", filename);
      }
    }
  }
}
