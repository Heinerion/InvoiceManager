package de.heinerion.invoice.repositories.template;

import de.heinerion.invoice.models.InvoiceTemplate;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

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