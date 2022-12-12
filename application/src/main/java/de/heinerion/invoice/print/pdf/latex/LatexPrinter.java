package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.contract.Contract;
import de.heinerion.invoice.boundary.HostSystem;
import de.heinerion.invoice.models.Conveyable;
import de.heinerion.invoice.print.Printer;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

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
  public void writeFile(Conveyable conveyable, Path targetFolder, String title) {
    Path workingDirectory = pathUtil.getWorkingDirectory();
    String texName = title + TEX;

    Path sourceFile = workingDirectory.resolve(texName);
    String content = latexGenerator.generateSourceContent(conveyable);
    hostSystem.writeToFile(sourceFile, content);

    hostSystem.pdfLatex(sourceFile);

    Path target = targetFolder.toAbsolutePath();
    moveSource(workingDirectory, pathUtil.switchToSystem(target), title + TEX);
    moveSource(workingDirectory, target, title + PDF);

    removeTempFiles(workingDirectory, title);

    log.atFine().log("%s created, temp files cleaned", sourceFile);
  }

  private void moveSource(Path sourceFolder, Path targetFolder, String fileName) {
    Contract.require(Files.exists(sourceFolder), "Source folder exists: %s".formatted(sourceFolder));
    Contract.require(Files.exists(targetFolder), "Target folder exists: %s".formatted(targetFolder));

    Path target = targetFolder.resolve(fileName);
    Path source = sourceFolder.resolve(fileName);

    move(source, target);
  }

  private void move(Path source, Path target) {
    Contract.require(Files.exists(source), "Source file exists: %s".formatted(source));

    try {
      log.atInfo().log("Move from %s to %s", source, target);
      Files.move(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
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
