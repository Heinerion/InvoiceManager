package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.contract.Contract;
import de.heinerion.util.*;
import lombok.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.function.*;

import static de.heinerion.invoice.view.swing.home.receiver.forms.ComponentFactory.*;

/**
 * Represents the idea of a Line in a Form.
 * <p>
 * Such Line consists of the following:
 * <ul>
 * <li>a "name" - think of a title</li>
 * <li>a validation method, whether given value is acceptable for the form's purpose</li>
 * <li>an embedded {@link JComponent} to handle its inputs and visualization</li>
 * <li>a getter function, to retrieve a value Entity of the raw input field value</li>
 * <li>a setter function, to transfer the value Entity to a business model</li>
 * <li>a hint component, to show a hint, if the given value is not valid</li>
 * </ul>
 * </p>
 *
 * @param <T> the domain entities type, to set the domain value on
 * @param <A> the domain type of the value represented by this {@link FormLine}
 */
@RequiredArgsConstructor
public class FormLine<T, A> {
  @Getter
  private final String name;
  private final Predicate<A> valid;
  private final JComponent component;
  private final Function<JComponent, A> getter;
  private final BiConsumer<T, A> setter;
  @Getter
  private final JLabel hintComponent = new JLabel();

  public boolean isValid() {
    return valid.test(getter.apply(component));
  }

  public void applyValue(T entity) {
    setter.accept(entity, getValue());
  }

  private void onChange() {
    showValidity();
  }

  private void addChangeListener(JComponent component) {
    if (component instanceof JTextComponent tf) {
      tf.getDocument().addDocumentListener(new SimpleDocumentListener(this::onChange));
    } else if (component instanceof JSpinner spinner) {
      spinner.addChangeListener(e -> onChange());
    }
  }

  public void showValidity() {
    if (isValid()) {
      setBackground(Color.WHITE);
      hintComponent.setVisible(false);
    } else {
      setBackground(Color.PINK);
      hintComponent.setVisible(true);
    }
  }

  private void setBackground(Color color) {
    component.setBackground(color);
  }

  protected A getValue() {
    return getter.apply(getComponent());
  }

  public JComponent getComponent() {
    Contract.ensureNotNull(component, "component of %ss".formatted(name));
    return component;
  }

  public static <T, A> FormLine<T, A> of(String name, BiConsumer<T, A> setter, Class<A> valueType, Predicate<A> valid, JComponent component) {
    String format = "%s of the form line %s";
    Contract.requireNotNull(name, format.formatted("name", "?"));
    Contract.requireNotNull(setter, format.formatted("setter", name));
    Contract.requireNotNull(valueType, format.formatted("type", name));
    Contract.requireNotNull(valid, format.formatted("valid", name));
    Contract.requireNotNull(component, format.formatted("component", name));

    Function<JComponent, A> getter = determineGetter(component, valueType);
    Contract.requireNotNull(getter, format.formatted("getter", name));

    return createLine(name, setter, valid, component, getter);
  }

  private static <T, A> FormLine<T, A> createLine(String name, BiConsumer<T, A> setter, Predicate<A> valid, JComponent component, Function<JComponent, A> getter) {
    FormLine<T, A> line = new FormLine<>(name, valid, component, getter, setter);
    line.addChangeListener(line.component);
    return line;
  }

  public static <X> FormLine<X, String> ofString(String name, BiConsumer<X, String> setter) {
    return FormLine.of(name, setter, String.class, Strings::isNotBlank, createStringComponent());
  }

  public static <X> FormLine<X, String> ofString(String name, BiConsumer<X, String> setter, int columns) {
    return FormLine.of(name, setter, String.class, Strings::isNotBlank, createStringComponent(columns));
  }

  public static <X> FormLine<X, Double> ofDouble(String name, BiConsumer<X, Double> setter) {
    return FormLine.of(name, setter, Double.class, Doubles::isGreaterZero, createDoubleComponent());
  }

  @Override
  public String toString() {
    return "FormLine{" +
        "name='" + name + '\'' +
        '}';
  }

  private record SimpleDocumentListener(Runnable r) implements DocumentListener {
    @Override
    public void insertUpdate(DocumentEvent e) {
      r.run();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      r.run();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      r.run();
    }
  }
}
