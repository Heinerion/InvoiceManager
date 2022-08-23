package de.heinerion.betriebe.repositories.template;

import de.heinerion.betriebe.models.InvoiceTemplate;
import de.heinerion.betriebe.repositories.JaxbManager;

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
  protected Class<?> getWrapper() {
    return TemplateListWrapper.class;
  }
}
