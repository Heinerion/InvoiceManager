package de.heinerion.invoice.tool.domain;

public class Product {
  private final String name;
  private String unit;
  private Euro pricePerUnit;
  private Percent taxes;

  public Product(String name) {
    this.name=name;
  }

  public void setUnit(String unit) {
this.unit=unit;
  }

  public void setPricePerUnit(Euro pricePerUnit) {
this.pricePerUnit=pricePerUnit;
  }

  public void setTaxes(Percent taxes) {
    this.taxes=taxes;
  }

  public String getName() {
    return name;
  }

  public String getUnit() {
    return unit;
  }

  public Euro getPricePerUnit() {
    return pricePerUnit;
  }

  public Percent getTaxes() {
    return taxes;
  }
}
