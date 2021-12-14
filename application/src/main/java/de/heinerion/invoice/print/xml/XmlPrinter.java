package de.heinerion.invoice.print.xml;

import com.thoughtworks.xstream.XStream;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.invoice.print.Printer;
import de.heinerion.invoice.print.pdf.boundary.HostSystem;
import lombok.extern.flogger.Flogger;

import java.io.File;

@Flogger
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
  public void writeFile(Letter letter, File parentFolder, String title) {
    String path = parentFolder.getPath() + File.separator + title + ".xml";
    String content = xstream.toXML(letter);
    File result = hostSystem.writeToFile(path, content);

    log.atInfo().log("%s written", result.getAbsolutePath());
  }
}
