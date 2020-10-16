package de.heinerion.invoice.view.swing.home.receiver.forms;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractForm<T> implements Form<T> {
  private JPanel content;
  private GridBagConstraints constraints;

  private JButton btnCheck;

  protected abstract List<FormLine<T, ?>> getProperties();

  protected abstract String getTitle();

  protected abstract T createInstance();

  @Override
  public final T getValue() {
    if (isValid()) {
      T result = createInstance();
      for (FormLine<T, ?> property : getProperties()) {
        property.applyValue(result);
      }
      return result;
    }

    return null;
  }

  @Override
  public final JPanel getPanel() {

    JPanel container = new JPanel(new BorderLayout());
    container.setBorder(BorderFactory.createTitledBorder(translate(getTitle())));
    container.add(createContent());
    container.add(btnCheck, BorderLayout.PAGE_END);

    return container;
  }

  private JPanel createContent() {
    initLayout();
    createWidgets();
    addWidgets();
    setupInteractions();

    return content;
  }

  private void initLayout() {
    content = new JPanel(new GridBagLayout());
    constraints = new GridBagConstraints();
  }

  private void createWidgets() {
    btnCheck = new JButton("check");
  }

  private void addWidgets() {
    int lineNumber = 0;
    for (FormLine<T, ?> property : getProperties()) {
      JLabel hintComponent = property.getHintComponent();
      String propertyName = property.getName();
      hintComponent.setText(translateHint(propertyName));

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
    }
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
    constraints.gridx = x;
    constraints.gridy = y;
    content.add(component, constraints);
  }

  private boolean isValid() {
    List<FormLine<T, ?>> invalidFields = getProperties().stream()
        .filter(p -> !p.isValid())
        .collect(Collectors.toList());
    return invalidFields.isEmpty();
  }

  private void setupInteractions() {
    btnCheck.addActionListener((__) -> System.out.println(getValue()));
  }
}
