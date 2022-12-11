package de.heinerion.invoice.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "item")
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private Integer position;

  private String name;
  private String unit;
  @Column(name = "price_per_unit")
  private Double pricePerUnit;
  private Double quantity;
  private Double total;

  public Item setPricePerUnit(double pricePerUnit) {
    this.pricePerUnit = pricePerUnit;
    this.updateValues();
    return this;
  }

  public Item setQuantity(double newQuantity) {
    this.quantity = newQuantity;
    this.updateValues();
    return this;
  }

  private void updateValues() {
    total = pricePerUnit != null && quantity != null
        ? pricePerUnit * quantity
        : null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item item = (Item) o;
    return id != null && Objects.equals(id, item.id)
        || Objects.equals(name, item.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  public static Item of(int position, String name, String unit, double pricePerUnit, double quantity) {
    return new Item()
        .setPosition(position)
        .setName(name)
        .setUnit(unit)
        .setPricePerUnit(pricePerUnit)
        .setQuantity(quantity)
        .setTotal(quantity * pricePerUnit);
  }

  public static Item of(int position, String message) {
    return new Item()
        .setPosition(position)
        .setName(message);
  }

  public static Item empty(int position) {
    return new Item()
        .setPosition(position);
  }
}
