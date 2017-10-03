package de.heinerion.betriebe.loading.jaxb;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class TemplateManager {
  private boolean beautify;

  public TemplateManager() {
    this(false);
  }

  public TemplateManager(boolean beautify) {
    this.beautify = beautify;
  }

  public void marshalTemplates(List<InvoiceTemplate> templates, File destination) {
    try {
      JAXBContext context = JAXBContext.newInstance(TemplateListWrapper.class);
      Marshaller m = context.createMarshaller();

      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, beautify);

      TemplateListWrapper wrapper = new TemplateListWrapper();
      wrapper.setTemplates(templates);

      m.marshal(wrapper, destination);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<InvoiceTemplate> unmarshal(File source) {
    try {
      JAXBContext context = JAXBContext.newInstance(TemplateListWrapper.class);
      Unmarshaller um = context.createUnmarshaller();

      TemplateListWrapper wrapper = (TemplateListWrapper) um.unmarshal(source);

      return wrapper.getTemplates();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
