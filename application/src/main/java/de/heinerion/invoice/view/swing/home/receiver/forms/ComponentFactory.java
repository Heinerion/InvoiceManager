package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.contract.ContractBrokenException;
import de.heinerion.invoice.domain.values.DvIban;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.function.Function;

final class ComponentFactory {

  private static final int TEXT_AREA_THRESHOLD = 40;

  private ComponentFactory() {
    // avoid instantiation
  }

  public static JComponent createStringComponent() {
    return createStringComponent(20);
  }

  public static JComponent createStringComponent(int columns) {
    return columns > TEXT_AREA_THRESHOLD
        ? createTextArea((columns / 20) + 1, 20)
        : createTextField(columns);
  }

  private static JComponent createTextArea(int rows, int columns) {
    return new JTextArea(rows, columns);
  }

  private static JComponent createTextField(int columns) {
    return new JTextField(columns);
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

    if (component instanceof JTextComponent textComponent) {
      return c -> (Y) textComponent.getText();
    }

    if (component instanceof JSpinner spinner) {
      return c -> (Y) spinner.getValue();
    }

    throw new ContractBrokenException("the getter for %s has been determined".formatted(valueType));
  }
}
