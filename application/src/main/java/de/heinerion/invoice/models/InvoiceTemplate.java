package de.heinerion.invoice.models;

import lombok.*;

import javax.persistence.*;
import java.text.Collator;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "template")
public class InvoiceTemplate implements Comparable<InvoiceTemplate> {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  private String name;

  @OneToMany(fetch = FetchType.EAGER)
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
    if (this == obj) {
      return true;
    }
    if (obj instanceof InvoiceTemplate other) {
      return id != null && id.equals(other.id)
          || compareTo(other) == 0;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName());
  }

  @Override
  public final String toString() {
    return this.getName();
  }
}
