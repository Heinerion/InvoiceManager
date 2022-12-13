package de.heinerion.invoice.models;

import lombok.*;

import javax.persistence.*;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

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

  @OneToMany(mappedBy = "template", fetch = FetchType.EAGER)
  Set<TemplateItem> templateItems = new HashSet<>();

  public InvoiceTemplate setTemplateItems(Set<TemplateItem> templateItems) {
    this.templateItems = Optional
        .ofNullable(templateItems)
        .orElse(Collections.emptySet())
        .stream().map(item -> item.setTemplate(this))
        .collect(Collectors.toSet());
    return this;
  }

  public List<Item> getItems() {
    return getTemplateItems().stream()
        .map(TemplateItem::asItem)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .sorted(Comparator.comparing(Item::getPosition))
        .toList();
  }

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
