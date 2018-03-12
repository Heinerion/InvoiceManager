package de.heinerion.invoice.view.swing.home.receiver.money;

public interface Money {
  Money add(Money money);

  Money divideBy(double parts);

  String getCurrency();

  double getValue();

  // Money parse(String text);

  Money sub(Money money);

  Money times(double times);
}