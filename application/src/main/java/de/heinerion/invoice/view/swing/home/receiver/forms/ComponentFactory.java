package de.heinerion.invoice.view.swing.home.receiver.forms;

import javax.swing.*;
import java.util.function.Function;

final class ComponentFactory {

  public static final int TEXT_AREA_THRESHOLD = 40;

  private ComponentFactory() {
    // avoid instantiation
  }

  public static Component<String, ? extends JComponent> createStringComponent() {
    return createStringComponent(20);
  }

  public static <T> Component<T, ? extends JComponent> createConverterComponent(int columns, Function<String, T> converter) {
    Component<String, ? extends JComponent> stringComponent = createStringComponent(columns);
    return new Component<>(
        stringComponent.component(),
        ignored -> converter.apply(stringComponent.getValue()));
  }

  public static Component<String, ? extends JComponent> createStringComponent(int columns) {
    return columns > TEXT_AREA_THRESHOLD
        ? createTextArea((columns / 20) + 1, 20)
        : createTextField(columns);
  }

  private static Component<String, ? extends JComponent> createTextArea(int rows, int columns) {
    return new Component<>(new JTextArea(rows, columns), JTextArea::getText);
  }

  private static Component<String, ? extends JComponent> createTextField(int columns) {
    return new Component<>(new JTextField(columns), JTextField::getText);
  }

  public static Component<Double, ? extends JComponent> createDoubleComponent() {
    SpinnerNumberModel numberModel = new SpinnerNumberModel(0.0, 0.0, 1000.0, 1);
    return new Component<>(new JSpinner(numberModel), spinner -> (Double) spinner.getValue());
  }

  public record Component<T, C extends JComponent>(C component, Function<C, T> getter) {
    public T getValue() {
      return getter.apply(component);
    }
  }
}
