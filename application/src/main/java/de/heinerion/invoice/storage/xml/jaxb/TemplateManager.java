package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.List;

public class TemplateManager extends JaxbManager<InvoiceTemplate> {
  @Override
  protected Object getRootObject() {
    return new TemplateListWrapper();
  }

  @Override
  protected void setContent(Object wrapper, List<InvoiceTemplate> items) {
    ((TemplateListWrapper) wrapper).setTemplates(items);
  }

  @Override
  protected List<InvoiceTemplate> getContent(Object rootObject) {
    return ((TemplateListWrapper) rootObject).getTemplates();
  }

  @Override
  protected JAXBContext getContext() throws JAXBException {
    return JAXBContext.newInstance(TemplateListWrapper.class);
  }
}
