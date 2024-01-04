package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.contract.Contract;
import de.heinerion.util.*;
import lombok.AllArgsConstructor;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.function.*;

import static de.heinerion.invoice.view.swing.home.receiver.forms.ComponentFactory.*;

@AllArgsConstructor
public class FormLine<T, A> {
  private String name;
  private Predicate<A> valid;

  private JComponent component;
  private Function<JComponent, A> getter;
  private BiConsumer<T, A> setter;
  private final JLabel hintComponent = new JLabel();

  public String getName() {
    return name;
  }

  public boolean isValid() {
    return getter != null
        && component != null
        && valid.test(getter.apply(component));
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

  public JLabel getHintComponent() {
    return hintComponent;
  }

  public static <T, A> FormLine<T, A> of(String name, BiConsumer<T, A> setter, Class<A> valueType, Predicate<A> valid, JComponent component) {
    FormLine<T, A> line = new FormLine<>(name, valid, component, ComponentFactory.determineGetter(component, valueType), setter);
    ensureAllFieldsAreSet(line);
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

  private static void ensureAllFieldsAreSet(FormLine<?, ?> line) {
    String format = "%s of the form line %s";
    Contract.requireNotNull(line.name, format.formatted("name", "?"));
    Contract.requireNotNull(line.setter, format.formatted("setter", line.name));
    Contract.requireNotNull(line.valid, format.formatted("valid", line.name));
    Contract.requireNotNull(line.component, format.formatted("component", line.name));
    Contract.requireNotNull(line.getter, format.formatted("getter", line.name));
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
