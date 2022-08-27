package de.heinerion.invoice.print.xml;

import com.thoughtworks.xstream.XStream;
import de.heinerion.invoice.boundary.HostSystem;
import de.heinerion.invoice.models.Conveyable;
import de.heinerion.invoice.print.Printer;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Flogger
@Service("XML")
public class XmlPrinter implements Printer {
  private final HostSystem hostSystem;
  private final XStream xstream;

  XmlPrinter(HostSystem hostSystem) {
    this.hostSystem = hostSystem;
    this.xstream = new XStream();

    configure();
  }

  private void configure() {
    XmlConfigurator xmlConfigurator = new XmlConfigurator(xstream);
    xmlConfigurator.setAliases();
    xmlConfigurator.setSecurityOptions();
  }

  @Override
  public void writeFile(Conveyable conveyable, Path parentFolder, String title) {
    Path path = parentFolder.resolve(title + ".xml");
    String content = xstream.toXML(conveyable);
    hostSystem.writeToFile(path, content);

    log.atInfo().log("%s written", path.toAbsolutePath());
  }
}
