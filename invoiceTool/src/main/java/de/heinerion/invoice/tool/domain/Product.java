package de.heinerion.invoice.tool.domain;

public class Product {
  private final String name;
  private String unit;
  private Euro pricePerUnit;
  private Percent taxes;

  public Product(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public Euro getPricePerUnit() {
    return pricePerUnit;
  }

  public void setPricePerUnit(Euro pricePerUnit) {
    this.pricePerUnit = pricePerUnit;
  }

  public Percent getTaxes() {
    return taxes;
  }

  public void setTaxes(Percent taxes) {
    this.taxes = taxes;
  }

  @Override
  public String toString() {
    return String.format("%s %s", name, getPricePerUnit());
  }
}
