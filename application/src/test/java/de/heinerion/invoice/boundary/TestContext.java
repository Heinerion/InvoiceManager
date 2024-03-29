package de.heinerion.invoice.boundary;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TestContext {
  private static List<String> messages;

  public static List<String> getMessages() {
    if (messages == null) {
      messages = new ArrayList<>();
    }

    return messages;
  }

  public static void addMessage(String message) {
    System.out.println(message);

    getMessages().add(message);
  }

  public static void assertEquals(List<String> expectedMessages) {
    assertArrayEquals(expectedMessages.toArray(new String[0]), TestContext.getMessages().toArray(new String[0]));
  }
}
