package de.heinerion.money;

public final class Euro extends AbstractMoney {
  protected Euro(double aValue) {
    super(aValue, "€");
  }

  @Override
  public Euro add(Money money) {
    Euro result = null;
    if (money instanceof Euro) {
      final double newValue = getValue() + money.getValue();
      result = new Euro(newValue);
    }
    // else könnte Umrechnungswerte suchen o.ä.
    return result;
  }

  @Override
  public Euro divideBy(double parts) {
    Euro result = null;
    final double newValue = getValue() / parts;
    result = new Euro(newValue);
    return result;
  }

  public static Euro parse(String text) {
    final double theValue = parseValue(text);
    return new Euro(theValue);
  }

  @Override
  public Euro sub(Money money) {
    Euro result = null;
    if (money instanceof Euro) {
      final double newValue = getValue() - money.getValue();
      result = new Euro(newValue);
    }
    // else könnte Umrechnungswerte suchen o.ä.
    return result;
  }

  @Override
  public Euro times(double times) {
    Euro result = null;
    final double newValue = getValue() * times;
    result = new Euro(newValue);
    return result;
  }
}
