package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.contract.ContractBrokenException;
import de.heinerion.invoice.domain.values.DvIban;

import javax.swing.*;
import java.util.function.Function;

final class ComponentFactory {

  private ComponentFactory() {
    // avoid instantiation
  }

  public static JTextField createStringComponent() {
    return createStringComponent(20);
  }

  public static JTextField createStringComponent(int columns) {
    // TODO if > threshold ? TextArea width = 20, height ) col / 20
    JTextField field = new JTextField();
    field.setColumns(columns);
    return field;
  }

  public static JSpinner createDoubleComponent() {
    return new JSpinner(new SpinnerNumberModel(0.0,
        0.0,
        1000.0,
        1));
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
