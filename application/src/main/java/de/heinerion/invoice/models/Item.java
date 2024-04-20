package de.heinerion.invoice.models;

import jakarta.persistence.*;
import lombok.*;

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

  @ManyToOne
  @JoinColumn(name = "invoice_id")
  private Invoice invoice;

  private Integer position;

  private String name;
  private String unit;
  @Column(name = "price_per_unit")
  private Double pricePerUnit;
  private Double quantity;
  private Double total;

  public Item setPricePerUnit(Double pricePerUnit) {
    this.pricePerUnit = pricePerUnit;
    this.updateValues();
    return this;
  }

  public Item setQuantity(Double newQuantity) {
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

  @Override
  public String toString() {
    return getName();
  }

  public Item copy() {
    return Item.copyOf(this);
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

  public static Item copyOf(Item item) {
    return new Item()
        .setPosition(item.getPosition())
        .setName(item.getName())
        .setUnit(item.getUnit())
        .setPricePerUnit(item.getPricePerUnit())
        .setQuantity(item.getQuantity());
  }
}
