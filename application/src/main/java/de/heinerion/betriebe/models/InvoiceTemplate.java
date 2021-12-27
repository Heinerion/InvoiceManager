package de.heinerion.betriebe.models;

import java.io.Serial;
import java.io.Serializable;
import java.text.Collator;
import java.util.Arrays;
import java.util.UUID;

public class InvoiceTemplate implements Serializable, Comparable<InvoiceTemplate> {
  @Serial
  private static final long serialVersionUID = 5654884407643922708L;

  private String name = "";
  private String[][] inhalt;
  private UUID companyId;

  @Override
  public final int compareTo(InvoiceTemplate o) {
    return (Collator.getInstance()).compare(this.getName(), o.getName());
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof InvoiceTemplate other
        && compareTo(other) == 0;
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }

  public UUID getCompanyId() {
    return companyId;
  }

  public void setCompanyId(UUID companyId) {
    this.companyId = companyId;
  }

  public final String[][] getInhalt() {
    return inhalt;
  }

  public final String getName() {
    return this.name;
  }

  public final void setInhalt(String[][] content) {
    this.inhalt = Arrays.copyOf(content, content.length);
  }

  public final void setName(String aName) {
    this.name = aName;
  }

  @Override
  public final String toString() {
    return this.getName();
  }
}
