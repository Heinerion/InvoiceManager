package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.contract.Contract;
import lombok.extern.flogger.Flogger;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Flogger
public abstract class AbstractForm<T> implements Form<T> {
  private JPanel content;
  private GridBagConstraints constraints;
  private int lineNumber;

  protected abstract List<FormLine<T, ?>> getProperties();

  protected abstract String getTitle();

  protected abstract T createInstance();

  @Override
  public final T getValue() {
    if (isValid()) {
      T result = createInstance();
      fillInstance(result);
      return result;
    }

    log.atFine().log("some property is not valid");
    return null;
  }

  private void fillInstance(T instance) {
    Contract.requireNotNull(instance, "instance");
    for (FormLine<T, ?> property : getProperties()) {
      log.atFiner().log("set %s on %s", property, instance);
      property.applyValue(instance);
    }
  }

  @Override
  public final JPanel getPanel() {

    JPanel container = new JPanel(new BorderLayout());
    container.setBorder(BorderFactory.createTitledBorder(translate(getTitle())));
    container.add(createContent());

    return container;
  }

  private JPanel createContent() {
    initLayout();
    addWidgets();

    return content;
  }

  private void initLayout() {
    content = new JPanel(new GridBagLayout());
    constraints = new GridBagConstraints();
  }

  private void addWidgets() {
    lineNumber = 0;
    getProperties().forEach(this::addProperty);
  }

  private void addProperty(FormLine<T, ?> property) {
    log.atFiner().log("add property %s", property);
    JLabel hintComponent = property.getHintComponent();
    String propertyName = property.getName();
    hintComponent.setText(translateHint(propertyName));

    Contract.requireNotNull(property.getComponent(), "component of %s".formatted(propertyName));

    // name and textbox
    constraints.insets = new Insets(5, 5, 1, 5);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    setToPosition(0, lineNumber, new JLabel(translateProperty(propertyName)));
    setToPosition(1, lineNumber, property.getComponent());
    lineNumber++;

    // placeholder and hint
    constraints.insets = new Insets(0, 5, 5, 5);
    setToPosition(0, lineNumber, new JLabel(" "));
    setToPosition(1, lineNumber, hintComponent);
    constraints.fill = GridBagConstraints.NONE;
    lineNumber++;

    property.showValidity();
  }

  private String translateHint(String attribute) {
    return translate(String.format("%s.%s.hint", getTitle(), attribute));
  }

  private String translateProperty(String attribute) {
    return translate(String.format("%s.%s", getTitle(), attribute));
  }

  private String translate(String key) {
    return Forms.translate(key);
  }

  private void setToPosition(int x, int y, JComponent component) {
    Contract.requireNotNull(component, "component");
    Contract.requireNotNull(constraints, "constraints");
    constraints.gridx = x;
    constraints.gridy = y;
    content.add(component, constraints);
  }

  private boolean isValid() {
    return getProperties().stream()
        .allMatch(FormLine::isValid);
  }

  public void setEditable(boolean isEditable) {
    getProperties().forEach(p -> p.getComponent().setEnabled(isEditable));
  }
}
