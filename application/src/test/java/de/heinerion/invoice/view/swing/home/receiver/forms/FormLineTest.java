package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.contract.ContractBrokenException;
import de.heinerion.util.Functions;
import lombok.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import javax.swing.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FormLineTest {
  @Test
  void of_nullsTriggerExceptions() {
    String name = "name";
    BiConsumer<Object, String> set = Functions::doNothing;
    Class<String> type = String.class;
    Predicate<String> valid = Functions::alwaysTrue;
    JComponent comp = new JTextArea();

    assertAll(
        assertContract(() -> FormLine.of(null, set, type, valid, comp), "name"),
        assertContract(() -> FormLine.of(name, null, type, valid, comp), "setter"),
        assertContract(() -> FormLine.of(name, set, null, valid, comp), "type"),
        assertContract(() -> FormLine.of(name, set, type, null, comp), "valid"),
        assertContract(() -> FormLine.of(name, set, type, valid, null), "component"),
        // JPanel can't be used to represent a value, that could be obtained.
        // That's why it can never be used to create a valid getter.
        assertContract(() -> FormLine.of(name, set, type, valid, new JPanel()), "getter")
    );
  }

  private static Executable assertContract(Executable action, String propertyName) {
    return () -> {
      ContractBrokenException exception = assertThrows(ContractBrokenException.class, action, propertyName);
      assertTrue(exception.getMessage().contains(propertyName),
          "exception message contains property name. [property:%s, msg:%s]"
              .formatted(propertyName, exception.getMessage()));
    };
  }

  @Test
  void of_StringRequiresTextComponent() {
    String name = "name";
    BiConsumer<Object, String> set = Functions::doNothing;
    Predicate<String> valid = Functions::alwaysTrue;

    assertAll(
        () -> {
          FormLine<Object, String> line = FormLine
              .of(name, set, String.class, valid, new JTextField());
          assertEquals(name, line.getName());
        },
        () -> {
          FormLine<Object, String> line = FormLine
              .of(name, set, String.class, valid, new JTextArea());
          assertEquals(name, line.getName());
        },
        () -> assertThrows(ContractBrokenException.class, () -> FormLine
            .of(name, set, String.class, valid, new JPanel()))
    );
  }

  @Test
  void validValues_applyValue() {
    Predicate<String> validator = Functions::alwaysTrue;
    FormLine<StringContainer, String> line = FormLine
        .of("name", StringContainer::setText, String.class, validator, new JTextField());

    StringContainer container = new StringContainer("sun");

    ((JTextField) line.getComponent()).setText("moon");
    line.applyValue(container);

    assertTrue(line.isValid());
    assertFalse(line.getHintComponent().isVisible());
    assertEquals("moon", container.getText());
  }

  @Test
  void validValues_hideHintComponent() {
    Predicate<String> validator = Functions::alwaysTrue;
    FormLine<StringContainer, String> line = FormLine
        .of("name", StringContainer::setText, String.class, validator, new JTextField());

    ((JTextField) line.getComponent()).setText("moon");

    assertTrue(line.isValid());
    assertFalse(line.getHintComponent().isVisible());
  }

  @Test
  void invalidValues_applyValue() {
    // TODO(hsp): should invalid values really be applied?
    // What to do instead? Null? Otherwise a null value needs to be defined.

    Predicate<String> validator = Functions::alwaysFalse;
    FormLine<StringContainer, String> line = FormLine
        .of("name", StringContainer::setText, String.class, validator, new JTextField());

    StringContainer container = new StringContainer("sun");

    ((JTextField) line.getComponent()).setText("moon");
    line.applyValue(container);

    assertFalse(line.isValid());
    assertEquals("moon", container.getText());
  }

  @Test
  void invalidValues_showHintComponent() {
    Predicate<String> validator = Functions::alwaysFalse;
    FormLine<StringContainer, String> line = FormLine
        .of("name", StringContainer::setText, String.class, validator, new JTextField());

    ((JTextField) line.getComponent()).setText("moon");

    assertFalse(line.isValid());
    assertTrue(line.getHintComponent().isVisible());
  }

  @Test
  void ofString_noLengthGiven_JTextField() {
    FormLine<StringContainer, String> line = FormLine.ofString("name", StringContainer::setText);
    assertEquals(JTextField.class, line.getComponent().getClass());
  }

  @Test
  void ofString_lengthGiven_JTextField() {
    FormLine<StringContainer, String> line = FormLine.ofString("name", StringContainer::setText, ComponentFactory.TEXT_AREA_THRESHOLD);
    assertEquals(JTextField.class, line.getComponent().getClass());
  }

  @Test
  void ofString_lengthGiven_JTextArea() {
    FormLine<StringContainer, String> line = FormLine.ofString("name", StringContainer::setText, ComponentFactory.TEXT_AREA_THRESHOLD + 1);
    assertEquals(JTextArea.class, line.getComponent().getClass());
  }

  @Test
  void ofString_JTextArea() {
    FormLine<StringContainer, String> line = FormLine.ofString("name", StringContainer::setText, ComponentFactory.TEXT_AREA_THRESHOLD + 1);

    StringContainer container = new StringContainer("sun");
    ((JTextArea) line.getComponent()).setText("moon");
    line.applyValue(container);
    assertEquals("moon", container.getText());
  }

  @Test
  void ofString_JTextField() {
    FormLine<StringContainer, String> line = FormLine.ofString("name", StringContainer::setText, ComponentFactory.TEXT_AREA_THRESHOLD);

    StringContainer container = new StringContainer("sun");
    ((JTextField) line.getComponent()).setText("moon");
    line.applyValue(container);
    assertEquals("moon", container.getText());
  }

  @Test
  void ofDouble_JSpinner() {
    FormLine<DoubleContainer, Double> line = FormLine.ofDouble("name", DoubleContainer::setNumber);
    assertEquals(JSpinner.class, line.getComponent().getClass());
  }

  @Test
  void ofDouble() {
    FormLine<DoubleContainer, Double> line = FormLine.ofDouble("name", DoubleContainer::setNumber);

    DoubleContainer container = new DoubleContainer(42d);
    ((JSpinner) line.getComponent()).setValue(1337d);
    line.applyValue(container);
    assertEquals(1337d, container.getNumber());
  }

  @Data
  @AllArgsConstructor
  private static final class StringContainer {
    private String text;
  }

  @Data
  @AllArgsConstructor
  private static final class DoubleContainer {
    private double number;
  }
}