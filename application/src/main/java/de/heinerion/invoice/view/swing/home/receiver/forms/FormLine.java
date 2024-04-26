package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.contract.Contract;
import de.heinerion.util.Strings;
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
 * @param <C> the type of the used {@link JComponent}
 */
@RequiredArgsConstructor
public class FormLine<T, A, C extends JComponent> {
  @Getter
  private final String name;
  private final Predicate<A> valid;
  private final C component;
  private final Function<C, A> getter;
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

  public C getComponent() {
    Contract.ensureNotNull(component, "component of %s".formatted(name));
    return component;
  }

  /**
   * Creates a {@link FormLine} for any values.
   * <p>
   * Please consider using specialized Factories (please consult the @see section)
   * </p>
   *
   * @param name      the name of this line
   * @param setter    {@link BiConsumer} to update the Entity with this lines value
   * @param valueType {@link Class} of the domain value, represented by this line
   * @param valid     {@link Predicate} to determine, if the given value will be accepted
   * @param component {@link JComponent} to be used internally, to edit the line's content
   * @param getter    {@link Function} to retrieve the domain value from the component
   * @param <T>       the type of the Entity to be updated with the setter
   * @param <A>       the type of the domain value
   *
   * @return a {@link FormLine} for any properties
   *
   * @see #of(String, BiConsumer, Predicate, ComponentFactory.Component)
   * @see #ofString(String, BiConsumer)
   * @see #ofString(String, BiConsumer, int)
   * @see #ofDouble(String, BiConsumer)
   */
  public static <T, A, C extends JComponent> FormLine<T, A, C> of(String name, BiConsumer<T, A> setter, Class<A> valueType, Predicate<A> valid, C component, Function<C, A> getter) {
    String format = "%s of the form line %s";
    Contract.requireNotNull(name, format.formatted("name", "?"));
    Contract.requireNotNull(setter, format.formatted("setter", name));
    Contract.requireNotNull(valueType, format.formatted("type", name));
    Contract.requireNotNull(valid, format.formatted("valid", name));
    Contract.requireNotNull(component, format.formatted("component", name));
    Contract.requireNotNull(getter, format.formatted("getter", name));

    return createLine(name, setter, valid, component, getter);
  }

  /**
   * Creates a {@link FormLine} for any values.
   * <p>
   * Please consider using specialized Factories (please consult the @see section)
   * </p>
   *
   * @param name      the name of this line
   * @param setter    {@link BiConsumer} to update the Entity with this lines value
   * @param valid     {@link Predicate} to determine, if the given value will be accepted
   * @param component {@link de.heinerion.invoice.view.swing.home.receiver.forms.ComponentFactory.Component} to be used
   *                  internally, to edit the line's content and retrieve its value
   * @param <T>       the type of the Entity to be updated with the setter
   * @param <A>       the type of the domain value
   *
   * @return a {@link FormLine} for any properties
   *
   * @see #of(String, BiConsumer, Class, Predicate, JComponent, Function)
   * @see #ofString(String, BiConsumer)
   * @see #ofString(String, BiConsumer, int)
   * @see #ofDouble(String, BiConsumer)
   */
  public static <T, A, C extends JComponent> FormLine<T, A, C> of(String name, BiConsumer<T, A> setter, Predicate<A> valid, ComponentFactory.Component<A, C> component) {
    Contract.requireNotNull(component, "component of the form line %s".formatted(name));
    return createLine(name, setter, valid, component.component(), component.getter());
  }

  private static <T, A, C extends JComponent> FormLine<T, A, C> createLine(String name, BiConsumer<T, A> setter, Predicate<A> valid, C component, Function<C, A> getter) {
    FormLine<T, A, C> line = new FormLine<>(name, valid, component, getter, setter);
    line.addChangeListener(line.component);
    return line;
  }

  /**
   * Creates a {@link FormLine} for String values.
   * <p>
   * String values are validated via {@link Strings#isNotBlank(String)}.
   * <br>
   * The {@link JComponent} to use, is determined by the {@link ComponentFactory}
   * </p>
   *
   * @param name   the name of this line
   * @param setter {@link BiConsumer} to update the Entity with this lines value
   * @param <X>    the type of the Entity to be updated with the setter
   *
   * @return a {@link FormLine} for String properties
   *
   * @see ComponentFactory#createStringComponent()
   */
  public static <X> FormLine<X, String, ? extends JComponent> ofString(String name, BiConsumer<X, String> setter) {
    return FormLine.of(name, setter, Strings::isNotBlank, createStringComponent());
  }

  /**
   * Creates a {@link FormLine} for String values.
   * <p>
   * String values are validated via {@link Strings#isNotBlank(String)}.
   * <br>
   * The {@link JComponent} to use, is determined by the {@link ComponentFactory} and the required columns / String
   * length
   * </p>
   *
   * @param name    the name of this line
   * @param setter  {@link BiConsumer} to update the Entity with this lines value
   * @param columns expected number of characters to be entered in this field (e.g. column size of the persistence
   *                mapping)
   * @param <X>     the type of the Entity to be updated with the setter
   *
   * @return a {@link FormLine} for String properties
   *
   * @see ComponentFactory#createStringComponent(int)
   */
  public static <X> FormLine<X, String, ? extends JComponent> ofString(String name, BiConsumer<X, String> setter, int columns) {
    return FormLine.of(name, setter, Strings::isNotBlank, createStringComponent(columns));
  }

  /**
   * Creates a {@link FormLine} for Double values.
   * <p>
   * Double values are received as valid, if grater than zero.
   * <br>
   * The {@link JComponent} to use, is determined by the {@link ComponentFactory}
   * </p>
   *
   * @param name   the name of this line
   * @param setter {@link BiConsumer} to update the Entity with this lines value
   * @param <X>    the type of the Entity to be updated with the setter
   *
   * @return a {@link FormLine} for Double properties
   *
   * @see ComponentFactory#createDoubleComponent()
   */
  public static <X> FormLine<X, Double, ? extends JComponent> ofDouble(String name, BiConsumer<X, Double> setter) {
    return FormLine.of(name, setter, s -> s > 0D, createDoubleComponent());
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
