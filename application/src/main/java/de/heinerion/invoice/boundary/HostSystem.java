package de.heinerion.invoice.boundary;

import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
}
