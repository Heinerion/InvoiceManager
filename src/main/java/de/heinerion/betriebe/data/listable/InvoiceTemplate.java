package de.heinerion.betriebe.data.listable;

import java.io.Serializable;
import java.text.Collator;
import java.util.Arrays;

public class InvoiceTemplate implements Serializable, Comparable<InvoiceTemplate>, DropListable {
  /**
   * Generierte ID
   */
  private static final long serialVersionUID = 5654884407643922708L;
  private String name = "";
  private String[][] inhalt;

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

  public final String[][] getInhalt() {
    return inhalt;
  }

  @Override
  public final String getName() {
    return this.name;
  }

  public final void setInhalt(String[][] content) {
    this.inhalt = Arrays.copyOf(content, content.length);
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
