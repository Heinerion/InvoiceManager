package de.heinerion.invoice.models;

interface Buyable {
  String getName();

  double getPricePerUnit();

  String getUnit();
}
