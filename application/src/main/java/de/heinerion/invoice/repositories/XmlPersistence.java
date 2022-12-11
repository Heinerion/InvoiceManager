package de.heinerion.invoice.repositories;

import de.heinerion.contract.Contract;
import de.heinerion.invoice.models.InvoiceTemplate;
import de.heinerion.invoice.repositories.template.TemplateManager;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.util.*;

@Service
public class XmlPersistence {
  public List<InvoiceTemplate> readTemplates(Path source) {
    return read(new TemplateManager(), source);
  }

  public void writeTemplates(Path destination, List<InvoiceTemplate> templates) {
    write(new TemplateManager(), templates, destination);
  }

  private static <T> List<T> read(JaxbManager<T> manager, Path source) {
    if (Files.exists(source)) {
      return manager.unmarshal(source);
    }

    return Collections.emptyList();
  }

  private static <T> void write(JaxbManager<T> manager, List<T> entries, Path destination) {
    Contract.require(destination.toString().endsWith("xml"), "destination '%s' ends with '.xml'".formatted(destination));
    manager
        .withFormattedOutput()
        .marshal(entries, destination);
  }
}
