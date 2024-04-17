package de.heinerion.invoice.boundary;

import de.heinerion.contract.Contract;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.Collections;

@Service
@Flogger
@RequiredArgsConstructor
public
class HostSystem {
  private final SystemCall systemCall;

  public void pdfLatex(Path tex) {
    log.atInfo().log("create pdf from latex sources");
    systemCall.pdfLatex(tex);
    // twice, for page numbering
    log.atInfo().log("recreate pdf to update references and page numbering");
    systemCall.pdfLatex(tex);
  }

  public void writeToFile(Path path, String content) {
    try {
      Files.write(path, Collections.singleton(content));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public boolean exists(Path path) {
    return Files.exists(path);
  }

  /**
   * Moves files on the file system.
   * <p>
   * Uses following {@link CopyOption CopyOptions}:
   * <li>{@link StandardCopyOption#REPLACE_EXISTING}</li>
   * <li>{@link StandardCopyOption#ATOMIC_MOVE}</li>
   *
   * @param src {@link Path} from where to copy
   * @param dst {@link Path} to copy to
   */
  public void moveFile(Path src, Path dst) {
    Contract.require(exists(src), "Source file exists: %s".formatted(src));

    try {
      log.atInfo().log("Move from %s to %s", src, dst);
      Files.move(src, dst, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    } catch (IOException ioe) {
      log.atWarning()
          .withCause(ioe)
          .log("File could not be moved to %s.", dst);
    }
  }

  public void deleteFile(Path filename) {
    try {
      Files.delete(filename);
    } catch (IOException e) {
      log.atWarning().log("%s could not be removed", filename);
    }
  }
}
