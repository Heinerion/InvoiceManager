package de.heinerion.invoice.view.swing.home.receiver.calculator;

import de.heinerion.invoice.view.swing.home.receiver.money.Money;

public class CalculationResult {
  private static final int PERCENT = 100;

  private final double taxes;

  private Money net;
  private Money vat;
  private Money gross;

  public Money getNet() {
    return net;
  }

  public Money getVat() {
    return vat;
  }

  public Money getGross() {
    return gross;
  }

  public static CalculationResult addTaxes(Money netAmount, double taxPercent) {
    return new CalculationResult(taxPercent).setNet(netAmount);
  }

  public static CalculationResult subtractTaxes(Money grossAmount, double taxPercent) {
    return new CalculationResult(taxPercent).setGross(grossAmount);
  }

  private CalculationResult(double taxes) {
    this.taxes = taxes / PERCENT;
  }

  private CalculationResult setNet(Money amount) {
    net = amount;
    vat = net.times(taxes);
    gross = net.add(vat);

    return this;
  }

  private CalculationResult setGross(Money amount) {
    gross = amount;
    net = gross.divideBy(1 + taxes);
    vat = gross.sub(net);

    return this;
  }
}
