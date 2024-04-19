package de.heinerion.invoice.boundary;

import de.heinerion.invoice.services.ConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.*;

@Flogger
@Service
@RequiredArgsConstructor
public class SystemCall {
  private final ProcessRunner processRunner;

  public boolean pdfLatex(Path tex) {
    log.atInfo().log("create pdf from latex sources");
    if (callPdfLatex(tex)) {
      // twice, for page numbering
      log.atInfo().log("recreate pdf to update references and page numbering");
      return callPdfLatex(tex);
    } else {
      log.atInfo().log("pdfLatex not installed");
      return false;
    }
  }

  boolean callPdfLatex(Path tex) {
    String command = ConfigurationService.get(ConfigurationService.PropertyKey.LATEX_COMMAND);
    List<String> arguments = new ArrayList<>(List.of(command.split(" ")));

    String fileArgument = ProcessRunner.quote(tex.toAbsolutePath().toString());
    arguments.add(fileArgument);
    log.atFine().log("command '%s'", Strings.join(arguments, ' '));

    String errorLogMessage = Boundary.translate("error.pdflatex");
    return processRunner.startProcess(errorLogMessage, arguments.toArray(String[]::new));
  }
}
