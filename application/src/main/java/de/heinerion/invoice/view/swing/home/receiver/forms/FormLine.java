package de.heinerion.invoice.view.swing.home.receiver.forms;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

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
    addChangeListener(component, () -> {
      checkValidity();
      showValidity();
    });
  }


  private void addChangeListener(JComponent component, Runnable r) {
    if (component instanceof JTextField) {
      JTextField tf = (JTextField) component;
      tf.getDocument().addDocumentListener(new DocumentListener() {
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
      });
    } else if (component instanceof JSpinner) {
      JSpinner spinner = (JSpinner) component;
      spinner.addChangeListener(e -> r.run());
    }
  }

  private void showValidity() {
    if (isValid()) {
      component.setBackground(Color.WHITE);
      hintComponent.setVisible(false);
    } else {
      component.setBackground(Color.PINK);
      hintComponent.setVisible(true);
    }
  }

  private void checkValidity() {
    valid = predicate.test(getValue());
  }

  private A getValue() {
    return getter.apply(getComponent());
  }

  public JComponent getComponent() {
    return component;
  }

  public JLabel getHintComponent() {
    return hintComponent;
  }

  public static <X, Y> Builder<X, Y> builder(Class<X> __, Class<Y> attribute) {
    return new Builder<X, Y>().component(determineComponent(attribute));
  }

  private static <Y> JComponent determineComponent(Class<Y> attribute) {
    if (attribute.equals(String.class)) {
      JTextField field = new JTextField();
      field.setColumns(20);
      return field;
    }

    if (attribute.equals(Double.class)) {
      return new JSpinner(new SpinnerNumberModel(10.0,
          0.0,
          1000.0,
          .1));
    }

    return null;
  }

  public static class Builder<X, Y> {
    private final FormLine<X, Y> line;

    Builder() {
      line = new FormLine<>();
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
      if (component instanceof JTextField) {
        return c -> (Y) ((JTextField) c).getText();
      }

      if (component instanceof JSpinner) {
        return c -> (Y) ((JSpinner) c).getValue();
      }

      return null;
    }

    public FormLine<X, Y> build() {
      ensureAllFieldsAreSet();
      return line;
    }

    private void ensureAllFieldsAreSet() {
      // TODO
    }
  }
}
