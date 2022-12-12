package de.heinerion.invoice.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.text.Collator;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "template")
public class InvoiceTemplate implements Serializable, Comparable<InvoiceTemplate> {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @Transient
  private Long companyId;

  private String name;

  @Transient
  // just for XML-Reading
  private String[][] inhalt;

  @OneToMany
  @JoinTable(name = "template_items",
      joinColumns = @JoinColumn(name = "template_id"),
      inverseJoinColumns = @JoinColumn(name = "item_id"))
  Set<Item> items = new HashSet<>();

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

  @Override
  public final String toString() {
    return this.getName();
  }
}
