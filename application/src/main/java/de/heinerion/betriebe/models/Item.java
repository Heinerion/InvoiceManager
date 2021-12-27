package de.heinerion.betriebe.models;

import java.util.Objects;

public final class Item implements Buyable {
  private String name;
  private String unit;
  private double pricePerUnit;
  private double quantity;
  private double total;

  /**
   * For persistence only
   */
  public Item() {
  }

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

  @Override
  public String getName() {
    return name;
  }

  public void setName(String newName) {
    this.name = newName;
  }

  @Override
  public double getPricePerUnit() {
    return pricePerUnit;
  }

  public void setPricePerUnit(double newPrice) {
    this.pricePerUnit = newPrice;
  }

  public double getQuantity() {
    return this.quantity;
  }

  public void setQuantity(double newQuantity) {
    this.quantity = newQuantity;
    this.updateValues();
  }

  public double getTotal() {
    return this.total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  @Override
  public String getUnit() {
    return unit;
  }

  public void setUnit(String newUnit) {
    unit = newUnit;
  }

  @Override
  public String toString() {
    return this.getName();
  }

  private void updateValues() {
    final double pricePerUnit = this.getPricePerUnit();
    this.total = pricePerUnit * this.quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
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
