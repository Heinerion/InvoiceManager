package de.heinerion.invoice.boundary;

import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
@Flogger
@RequiredArgsConstructor
public
class HostSystem {
  private final FileHandler fileHandler;
  private final SystemCall systemCall;

  public void pdfLatex(File tex) {
    log.atInfo().log("create pdf from latex sources");
    systemCall.pdfLatex(tex);
    // twice, for page numbering
    log.atInfo().log("recreate pdf to update references and page numbering");
    systemCall.pdfLatex(tex);
  }

  public void writeToFile(Path path, String content) {
    fileHandler.writeToFile(path, content);
  }
}
