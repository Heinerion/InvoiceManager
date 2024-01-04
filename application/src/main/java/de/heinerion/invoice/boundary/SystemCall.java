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
class SystemCall {
  private final ProcessRunner processRunner;

  void pdfLatex(Path tex) {
    String command = ConfigurationService.get(ConfigurationService.PropertyKey.LATEX_COMMAND);
    List<String> arguments = new ArrayList<>(List.of(command.split(" ")));

    String fileArgument = processRunner.quote(tex.toAbsolutePath().toString());
    arguments.add(fileArgument);
    log.atFine().log("command '%s'", Strings.join(arguments, ' '));

    String errorLogMessage = Boundary.translate("error.pdflatex");
    processRunner.startProcess(errorLogMessage, arguments.toArray(String[]::new));
  }
}
