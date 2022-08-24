package de.heinerion.invoice.repositories.invoice;

import de.heinerion.invoice.models.Invoice;
import de.heinerion.invoice.repositories.JaxbManager;

import java.util.List;

public class InvoiceManager extends JaxbManager<Invoice> {
  @Override
  protected Object getRootObject() {
    return new InvoiceListWrapper();
  }

  @Override
  protected void setContent(Object wrapper, List<Invoice> items) {
    ((InvoiceListWrapper) wrapper).setInvoices(items);
  }

  @Override
  protected List<Invoice> getContent(Object rootObject) {
    return ((InvoiceListWrapper) rootObject).getInvoices();
  }

  @Override
  protected Class<?> getWrapper() {
    return InvoiceListWrapper.class;
  }
}
