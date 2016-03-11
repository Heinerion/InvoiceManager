package de.heinerion.betriebe.models;

import de.heinerion.betriebe.models.interfaces.Buyable;

public final class Product implements Buyable {

  private final String name;
  private final String unit;
  private final double pricePerUnit;

  /**
   * TODO Nützlich? Welchen Mehrwert hat `Product`?
   */
  public Product(String aName, String aUnit, double thePricePerUnit) {
    this.name = aName;
    this.unit = aUnit;
    this.pricePerUnit = thePricePerUnit;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public double getPricePerUnit() {
    return this.pricePerUnit;
  }

  @Override
  public String getUnit() {
    return this.unit;
  }

}
