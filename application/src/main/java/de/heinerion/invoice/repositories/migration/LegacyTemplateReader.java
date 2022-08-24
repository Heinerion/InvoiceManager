package de.heinerion.invoice.repositories.migration;

import de.heinerion.invoice.models.InvoiceTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Arrays;

class LegacyTemplateReader extends ObjectInputStream {
  LegacyTemplateReader(InputStream inputStream) throws IOException {
    super(inputStream);
  }

  @Override
  protected java.io.ObjectStreamClass readClassDescriptor()
      throws IOException, ClassNotFoundException {
    ObjectStreamClass desc = super.readClassDescriptor();

    if (isOldInvoiceTemplate(desc.getName())) {
      desc = ObjectStreamClass.lookup(InvoiceTemplate.class);
    }

    return desc;
  }

  private boolean isOldInvoiceTemplate(String qualifiedName) {
    return Arrays.asList(
            "de.heinerion.betriebe.classes.texting.Vorlage",
            "de.heinerion.invoice.data.Vorlage",
            "de.heinerion.invoice.data.listable.Vorlage")
        .contains(qualifiedName);
  }
}
