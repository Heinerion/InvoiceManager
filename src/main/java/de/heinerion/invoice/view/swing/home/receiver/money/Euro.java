package de.heinerion.invoice.view.swing.home.receiver.money;

public final class Euro extends AbstractMoney {
  private Euro(double aValue) {
    super(aValue, "€");
  }

  public static Euro parse(String text) {
    final double theValue = parseValue(text);
    return new Euro(theValue);
  }

  public static Euro of(double theValue) {
    return new Euro(theValue);
  }

  @Override
  public Euro add(Money money) {
    if (money instanceof Euro) {
      final double newValue = getValue() + money.getValue();
      return new Euro(newValue);
    }
    // else could look for conversion rate or such
    throw new OperationNotYetImplementedException();
  }

  @Override
  public Euro divideBy(double parts) {
    final double newValue = getValue() / parts;
    return new Euro(newValue);
  }

  @Override
  public Euro sub(Money money) {
    if (money instanceof Euro) {
      final double newValue = getValue() - money.getValue();
      return new Euro(newValue);
    }
    // else could look for conversion rate or such
    throw new OperationNotYetImplementedException();
  }

  @Override
  public Euro times(double times) {
    final double newValue = getValue() * times;
    return new Euro(newValue);
  }

  private class OperationNotYetImplementedException extends RuntimeException {
  }
}
