package de.heinerion.betriebe.repositories.invoice;

import de.heinerion.betriebe.models.Invoice;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

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