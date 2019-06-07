package de.heinerion.invoice.print.pdf.boundary;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

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
