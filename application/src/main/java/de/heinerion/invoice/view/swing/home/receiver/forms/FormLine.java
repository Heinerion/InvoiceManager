package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.contract.Contract;
import lombok.AllArgsConstructor;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.synth.SynthFormattedTextFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.function.*;

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

  public static <T, A> FormLine<T, A> of(String name, Class<A> valueType, BiConsumer<T, A> setter, Predicate<A> valid) {
    JComponent component = ComponentFactory.determineComponent(valueType);
    FormLine<T, A> line = new FormLine<>(name, valid, component, ComponentFactory.determineGetter(component, valueType), setter);
    ensureAllFieldsAreSet(line);
    line.addChangeListener(line.component);
    return line;
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
