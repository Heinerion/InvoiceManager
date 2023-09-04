package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.contract.ContractBrokenException;
import de.heinerion.invoice.domain.values.DvIban;

import javax.swing.*;
import java.util.function.Function;

final class ComponentFactory {

  private ComponentFactory() {
    // avoid instantiation
  }

  public static <Y> JComponent determineComponent(Class<Y> attribute) {
    if (attribute.equals(String.class)) {
      JTextField field = new JTextField();
      field.setColumns(20);
      return field;
    }

    if (attribute.equals(DvIban.class)) {
      JTextField field = new JTextField();
      field.setColumns(27);
      return field;
    }

    if (attribute.equals(Double.class)) {
      return new JSpinner(new SpinnerNumberModel(0.0,
          0.0,
          1000.0,
          1));
    }

    throw new ContractBrokenException("a component has been found for %s".formatted(attribute));
  }

  public static <Y> Function<JComponent, Y> determineGetter(JComponent component, Class<Y> valueType) {
    if (valueType.isAssignableFrom(DvIban.class)
        && component instanceof JTextField jTextField) {
      return c -> (Y) DvIban.of(jTextField.getText());
    }

    if (component instanceof JTextField jTextField) {
      return c -> (Y) jTextField.getText();
    }

    if (component instanceof JSpinner spinner) {
      return c -> (Y) spinner.getValue();
    }

    throw new ContractBrokenException("the getter for %s has been determined".formatted(valueType));
  }
}
