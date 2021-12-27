package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.models.InvoiceTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    return Stream
        .of("de.heinerion.betriebe.classes.texting.Vorlage",
            "de.heinerion.betriebe.data.Vorlage",
            "de.heinerion.betriebe.data.listable.Vorlage")
        .collect(Collectors.toList())
        .contains(qualifiedName);
  }
}
