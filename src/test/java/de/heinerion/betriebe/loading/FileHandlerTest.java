package de.heinerion.betriebe.loading;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class FileHandlerTest {
  @Test
  public void testIsListOf() {
    final List<String> list = new ArrayList<>();

    try {
      final Method isListOf = FileHandler.class.getDeclaredMethod("isListOf", Object.class, Class.class);
      isListOf.setAccessible(true);

      Assert.assertFalse((Boolean) isListOf.invoke(null, list, String.class));

      list.add("Hundekuchen");
      Assert.assertTrue((Boolean) isListOf.invoke(null, list, String.class));
      Assert.assertFalse((Boolean) isListOf.invoke(null, list, Integer.class));
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      throw new IsListOfCouldNotBeInvokedException(e);
    }
  }

  private static class IsListOfCouldNotBeInvokedException extends RuntimeException {
    IsListOfCouldNotBeInvokedException(Throwable t) {
      super(t);
    }
  }
}
