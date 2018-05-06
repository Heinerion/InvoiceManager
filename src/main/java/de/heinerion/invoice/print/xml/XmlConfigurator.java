package de.heinerion.invoice.print.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import de.heinerion.betriebe.models.*;

import java.util.Collection;

public class XmlConfigurator {
  private final XStream xstream;

  public XmlConfigurator(XStream xstream) {
    this.xstream = xstream;
  }

  public void setAliases() {
    xstream.alias("invoice", Invoice.class);
    xstream.alias("item", Item.class);
    xstream.alias("company", Company.class);
    xstream.alias("address", Address.class);
    xstream.alias("account", Account.class);
  }

  public void setSecurityOptions() {
    XStream.setupDefaultSecurity(xstream);
    xstream.addPermission(NoTypePermission.NONE);
    xstream.addPermission(NullPermission.NULL);
    xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
    xstream.allowTypeHierarchy(Collection.class);
    xstream.allowTypesByWildcard(new String[] {
        "de.heinerion.**"
    });
  }
}
