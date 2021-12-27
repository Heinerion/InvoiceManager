package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.models.Invoice;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "invoices")
class InvoiceListWrapper {
  private List<Invoice> invoices;

  @XmlElement(name = "invoices")
  public List<Invoice> getInvoices() {
    return invoices;
  }

  public void setInvoices(List<Invoice> invoices) {
    this.invoices = invoices;
  }
}