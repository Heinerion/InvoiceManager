package de.heinerion.betriebe.models;

import de.heinerion.betriebe.models.interfaces.Buyable;

public final class Item implements Buyable {
  private Product product;
  private double quantity;
  private double total;

  public Item(Product newProduct) {
    this(newProduct, 0);
  }

  public Item(Product newProduct, double aQuantity) {
    this.product = newProduct;
    this.quantity = aQuantity;
    this.updateValues();
  }

  public void addQuantity(double additionalQuantity) {
    this.quantity += additionalQuantity;
    this.updateValues();
  }

  @Override
  public String getName() {
    return this.product.getName();
  }

  @Override
  public double getPricePerUnit() {
    return this.product.getPricePerUnit();
  }

  public double getQuantity() {
    return this.quantity;
  }

  public double getTotal() {
    return this.total;
  }

  @Override
  public String getUnit() {
    return this.product.getUnit();
  }

  public void setName(String newName) {
    product = new Product(newName, getUnit(), getPricePerUnit());
  }

  public void setPricePerUnit(double newPrice) {
    product = new Product(getName(), getUnit(), newPrice);
  }

  public void setQuantity(double newQuantity) {
    this.quantity = newQuantity;
    this.updateValues();
  }

  public void setUnit(String newUnit) {
    product = new Product(getName(), newUnit, getPricePerUnit());
  }

  @Override
  public String toString() {
    return this.getName();
  }

  private void updateValues() {
    final double pricePerUnit = this.getPricePerUnit();
    this.total = pricePerUnit * this.quantity;
  }
}
