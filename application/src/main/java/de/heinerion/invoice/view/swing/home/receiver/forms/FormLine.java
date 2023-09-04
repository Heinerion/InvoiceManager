package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.contract.*;
import de.heinerion.invoice.domain.values.DvIban;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.synth.SynthFormattedTextFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.function.*;

public class FormLine<T, A> {
  private String name;
  private boolean valid;
  private Predicate<A> predicate;

  private JComponent component;
  private Function<JComponent, A> getter;
  private BiConsumer<T, A> setter;
  private final JLabel hintComponent = new JLabel();

  private FormLine() {
  }

  public String getName() {
    return name;
  }

  public boolean isValid() {
    return valid;
  }

  public void applyValue(T entity) {
    setter.accept(entity, getValue());
  }

  public void setComponent(JComponent component) {
    this.component = component;
    addChangeListener(component);
  }

  private void onChange() {
    checkValidity();
    showValidity();
  }

  private void addChangeListener(JComponent component) {
    if (component instanceof JTextField tf) {
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
    if (component instanceof JSpinner spinner) {
      // this makes me sorry for using Nimbus L&F
      setSpinnerColor(spinner, color);
      return;
    }

    component.setBackground(color);
  }

  /**
   * Sets the background color of a JSpinner
   *
   * @see <a href="https://stackoverflow.com/a/35140636">https://stackoverflow.com/a/35140636</a>
   */
  private void setSpinnerColor(JSpinner spinner, Color color) {
    final JComponent editor = spinner.getEditor();
    int c = editor.getComponentCount();
    for (int i = 0; i < c; i++) {
      final Component comp = editor.getComponent(i);
      if (comp instanceof JTextComponent jTextComponent) {
        jTextComponent.setUI(new SynthFormattedTextFieldUI() {
          @Override
          protected void paint(javax.swing.plaf.synth.SynthContext context, Graphics g) {
            g.setColor(color);
            g.fillRect(3, 3, getComponent().getWidth() - 3, getComponent().getHeight() - 6);
            super.paint(context, g);
          }
        });
      }
    }
  }

  private void checkValidity() {
    valid = predicate.test(getValue());
  }

  private A getValue() {
    return getter.apply(getComponent());
  }

  public JComponent getComponent() {
    Contract.ensureNotNull(component, "component of %ss".formatted(name));
    return component;
  }

  public JLabel getHintComponent() {
    return hintComponent;
  }

  public static <X, Y> Builder<X, Y> of(Class<X> type, Class<Y> attribute) {
    return new Builder<X, Y>()
        .type(attribute)
        .component(determineComponent(attribute));
  }

  private static <Y> JComponent determineComponent(Class<Y> attribute) {
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

  @Override
  public String toString() {
    return "FormLine{" +
        "name='" + name + '\'' +
        '}';
  }

  public static class Builder<X, Y> {
    private final FormLine<X, Y> line;
    private Class<Y> valueType;

    Builder() {
      line = new FormLine<>();
    }

    public Builder<X, Y> type(Class<Y> type) {
      this.valueType = type;
      return this;
    }

    public Builder<X, Y> name(String name) {
      line.name = name;
      return this;
    }

    public Builder<X, Y> setter(BiConsumer<X, Y> setter) {
      line.setter = setter;
      return this;
    }

    public Builder<X, Y> valid(Predicate<Y> condition) {
      line.predicate = condition;
      return this;
    }

    public Builder<X, Y> component(JComponent component) {
      line.setComponent(component);
      line.getter = determineGetter(component);
      return this;
    }

    private Function<JComponent, Y> determineGetter(JComponent component) {
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

      throw new ContractBrokenException("the getter for %s of type %s has been determined"
          .formatted(line.name, valueType));
    }

    public FormLine<X, Y> build() {
      ensureAllFieldsAreSet();
      return line;
    }

    private void ensureAllFieldsAreSet() {
      String format = "%s of the form line %s";
      Contract.requireNotNull(line.name, format.formatted("name", "?"));
      Contract.requireNotNull(line.setter, format.formatted("setter", line.name));
      Contract.requireNotNull(line.predicate, format.formatted("predicate", line.name));
      Contract.requireNotNull(line.component, format.formatted("component", line.name));
      Contract.requireNotNull(line.getter, format.formatted("getter", line.name));
    }
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
