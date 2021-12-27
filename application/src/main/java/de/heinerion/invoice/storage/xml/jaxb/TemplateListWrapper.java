package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.models.InvoiceTemplate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "templates")
class TemplateListWrapper {
  private List<InvoiceTemplate> templates;

  @XmlElement(name = "template")
  public List<InvoiceTemplate> getTemplates() {
    return templates;
  }

  public void setTemplates(List<InvoiceTemplate> templates) {
    this.templates = templates;
  }
}