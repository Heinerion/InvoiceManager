package de.heinerion.invoice.view.swing.menu.tablemodels.archive;

import com.thoughtworks.xstream.XStream;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Item;
import de.heinerion.invoice.print.xml.XmlConfigurator;
import de.heinerion.invoice.view.DateUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Stream;

public final class XmlArchivedInvoice implements ArchivedInvoice {
  private static final String DIVIDER = "\t";

  private final File xml;
  private final XStream xstream;

  private Invoice original;

  public XmlArchivedInvoice(File sourceFile) {
    xml = sourceFile;
    this.xstream = new XStream();
  }


  @Override
  public void loadFile() throws IOException {
    configure();

    original = (Invoice) xstream.fromXML(xml);
  }

  private void configure() {
    XmlConfigurator xmlConfigurator = new XmlConfigurator(xstream);
    xmlConfigurator.setAliases();
    xmlConfigurator.setSecurityOptions();
  }

  @Override
  public double getAmount() {
    return original.getGross();
  }

  @Override
  public Company getCompany() {
    return original.getCompany();
  }

  @Override
  public LocalDate getDate() {
    return original.getDate();
  }

  @Override
  public int getInvoiceNumber() {
    return original.getNumber();
  }

  @Override
  public String getItem() {
    return "XML: " + original.getItems().stream()
        .map(Item::toString)
        .reduce((a, b) -> a + ", " + b)
        .orElse("");
  }

  @Override
  public File getFile() {
    return xml;
  }

  @Override
  public Address getRecipient() {
    return original.getReceiver();
  }

  @Override
  public String toString() {
    return Stream
        .of(getInvoiceNumber(), getRecipient(), DateUtil.format(getDate()), getCompany(), xml != null ? xml.getPath() : null)
        .map(x -> x != null ? x.toString() : "-")
        .reduce((x,y)-> x + DIVIDER + y)
        .orElse("");
  }
}
