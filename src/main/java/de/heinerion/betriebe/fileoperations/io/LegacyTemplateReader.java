package de.heinerion.betriebe.fileoperations.io;

import de.heinerion.betriebe.data.listable.Vorlage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

class LegacyTemplateReader extends ObjectInputStream {
  LegacyTemplateReader(InputStream inputStream) throws IOException {
    super(inputStream);
  }

  @Override
  protected java.io.ObjectStreamClass readClassDescriptor()
      throws IOException, ClassNotFoundException {
    ObjectStreamClass desc = super.readClassDescriptor();
    if ("de.heinerion.betriebe.classes.texting.Vorlage".equals(desc.getName())) {
      return ObjectStreamClass.lookup(Vorlage.class);
    }
    if ("de.heinerion.betriebe.data.Vorlage".equals(desc.getName())) {
      return ObjectStreamClass.lookup(Vorlage.class);
    }
    return desc;
  }
}
