package de.heinerion.betriebe.data.listable;

import java.io.Serializable;
import java.text.Collator;
import java.util.Arrays;

public class InvoiceTemplate implements Serializable, Comparable<InvoiceTemplate>, DropListable {
  private static final long serialVersionUID = 5654884407643922708L;
  private String name = "";
  private String[][] content;

  @Override
  public final int compareTo(InvoiceTemplate o) {
    return (Collator.getInstance()).compare(this.getName(), o.getName());
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof InvoiceTemplate && compareTo((InvoiceTemplate) obj) == 0;
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }

  public final String[][] getContent() {
    return content;
  }

  @Override
  public final String getName() {
    return this.name;
  }

  public final void setContent(String[][] content) {
    this.content = Arrays.copyOf(content, content.length);
  }

  @Override
  public final void setName(String aName) {
    this.name = aName;
  }

  @Override
  public final String toString() {
    return this.getName();
  }
}
