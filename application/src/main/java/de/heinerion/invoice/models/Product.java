package de.heinerion.invoice.models;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String name;
  private String unit;
  @Column(name = "price_per_unit")
  private Double pricePerUnit;

  @OneToMany(mappedBy = "product")
  private Set<TemplateItem> templateItems = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Product product = (Product) o;
    return id != null && Objects.equals(id, product.id)
        || Objects.equals(name, product.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return getName();
  }

  public static Product of(String name, String unit, double pricePerUnit) {
    return new Product()
        .setName(name)
        .setUnit(unit)
        .setPricePerUnit(pricePerUnit);
  }

  public static Product of(String message) {
    return new Product()
        .setName(message);
  }

  public static Product empty() {
    return new Product();
  }

}
