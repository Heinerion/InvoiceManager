package de.heinerion.betriebe.tools;

import java.util.ArrayList;
import java.util.List;

public final class ArrayTools {
  private ArrayTools() {
  }

  public static String arraytoString(Object[] array) {
    final List<String> items = new ArrayList<>();
    for (Object object : array) {
      if (object != null) {
        items.add(object.toString());
      }
    }
    return embrace(String.join(",", items));
  }

  public static String arraytoString(Object[][] arrays) {
    final List<String> items = new ArrayList<>();
    for (Object[] array : arrays) {
      if (array != null) {
        items.add(arraytoString(array));
      }
    }
    return embrace(String.join(",", items));
  }

  private static String embrace(String text) {
    return "{" + text + "}";
  }
}
