package de.heinerion.invoice.print.xml;

import com.thoughtworks.xstream.XStream;
import de.heinerion.betriebe.listener.Printer;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.invoice.print.pdf.boundary.HostSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class XmlPrinter implements Printer {
  private Logger logger = LogManager.getLogger(XmlPrinter.class);

  private final HostSystem hostSystem;
  private final XStream xstream;

  @Autowired
  XmlPrinter(HostSystem hostSystem){
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

    logger.info(result.getAbsolutePath() + " written");
  }
}
