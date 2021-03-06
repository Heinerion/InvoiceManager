package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TemplateManagerTest {
  private TemplateManager manager;
  private List<InvoiceTemplate> templates;

  @Before
  public void setUp() throws Exception {
    templates = new ArrayList<>();
    InvoiceTemplate template = new InvoiceTemplate();
    template.setName("template");
    templates.add(template);
    InvoiceTemplate templateB = new InvoiceTemplate();
    templateB.setName("templateB");
    templates.add(templateB);
  }

  @Test
  public void roundTripBeauty() throws Exception {
    manager = new TemplateManager();
    manager.setBeautify(true);

    File out = new File("templatesBeauty.xml");
    manager.marshal(templates, out);

    List<InvoiceTemplate> result = manager.unmarshal(out);
    boolean deleted = out.delete();

    Assert.assertTrue(deleted);
    Assert.assertEquals(map(templates), map(result));
  }

  private List<String> map(List<InvoiceTemplate> list) {
    return list.stream()
        .map(InvoiceTemplate::toString)
        .collect(Collectors.toList());
  }

  @Test
  public void roundTripUgly() throws Exception {
    manager = new TemplateManager();

    File out = new File("templatesUgly.xml");
    manager.marshal(templates, out);

    List<InvoiceTemplate> result = manager.unmarshal(out);
    boolean deleted = out.delete();

    Assert.assertTrue(deleted);
    Assert.assertEquals(map(templates), map(result));
  }
}