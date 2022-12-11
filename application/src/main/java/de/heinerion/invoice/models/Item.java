package de.heinerion.invoice.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "item")
public class Item implements Buyable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String name;
  private String unit;
  @Column(name = "price")
  private double pricePerUnit;
  private double quantity;
  private double total;

  public Item(String name, String unit, double pricePerUnit) {
    this(name, unit, pricePerUnit, 0);
  }

  public Item(String name, String unit, double pricePerUnit, double aQuantity) {
    this.name = name;
    this.unit = unit;
    this.pricePerUnit = pricePerUnit;
    this.quantity = aQuantity;
    this.updateValues();
  }

  public void addQuantity(double additionalQuantity) {
    this.quantity += additionalQuantity;
    this.updateValues();
  }

  public void setQuantity(double newQuantity) {
    this.quantity = newQuantity;
    this.updateValues();
  }

  private void updateValues() {
    total = pricePerUnit * quantity;
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
    return Double.compare(item.quantity, quantity) == 0 &&
        Double.compare(item.total, total) == 0 &&
        Objects.equals(name, item.name) &&
        Objects.equals(pricePerUnit, item.pricePerUnit) &&
        Objects.equals(unit, item.unit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, pricePerUnit, unit, quantity, total);
  }
}
