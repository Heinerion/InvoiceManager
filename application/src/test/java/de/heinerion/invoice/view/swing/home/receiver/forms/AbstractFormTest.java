package de.heinerion.invoice.view.swing.home.receiver.forms;

import lombok.*;
import org.junit.Test;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractFormTest {

  @Test
  public void getValue() {
    Form<TestItem> form = new GenericForm<>(
        TestItem::new,
        Arrays.asList(
            new ConstantFormLine<>(TestItem::setName, "stuff"),
            new ConstantFormLine<>(TestItem::setAmount, 1)
        ));

    TestItem item = form.getValue().orElse(null);

    assertNotNull(item);
    assertEquals(1, item.getAmount());
    assertEquals("stuff", item.getName());
  }

  @RequiredArgsConstructor
  private static class GenericForm<T> extends AbstractForm<T> {

    private final Supplier<T> generator;
    private final List<FormLine<T, ?>> properties;

    @Override
    protected List<FormLine<T, ?>> getProperties() {
      return properties;
    }

    @Override
    protected String getTitle() {
      return "generic";
    }

    @Override
    protected T createInstance() {
      return generator.get();
    }
  }

  @Data
  private static class TestItem {
    private String name;
    private int amount;
  }

  private static class ConstantFormLine<T, A> extends FormLine<T, A> {

    private final A value;

    public ConstantFormLine(BiConsumer<T, A> setter, A value) {
      super("name", x -> true, null, null, setter);
      this.value = value;
    }

    @Override
    public boolean isValid() {
      return true;
    }

    @Override
    protected A getValue() {
      return value;
    }
  }
}