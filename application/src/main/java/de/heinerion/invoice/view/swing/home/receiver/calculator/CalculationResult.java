package de.heinerion.invoice.view.swing.home.receiver.calculator;

import de.heinerion.invoice.view.swing.home.receiver.money.Money;

public record CalculationResult(double taxes, Money net, Money vat, Money gross) {
  private static final int PERCENT = 100;

  public static CalculationResult ofNet(Money netAmount, double taxPercent) {
    var tax = taxPercent / PERCENT;
    var vat = netAmount.times(tax);
    return new CalculationResult(tax, netAmount, vat, netAmount.add(vat));
  }

  public static CalculationResult ofGross(Money grossAmount, double taxPercent) {
    var vat = taxPercent / PERCENT;
    var net = grossAmount.divideBy(1 + vat);
    return new CalculationResult(vat, net, grossAmount.sub(net), grossAmount);
  }
}
