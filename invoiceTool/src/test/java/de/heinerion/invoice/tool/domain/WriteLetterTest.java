package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.boundary.DataStore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test based on the following Story:
 * <p>
 * <b>As a</b> salesman <br>
 * <b>I want to</b> write a free text letter and save it <br>
 * <b>so that</b> I can print and send it later
 * </p>
 */
public class WriteLetterTest {
  private DataStore dataStore;

  @Before
  public void setUp() {
    dataStore = new DataStore();
  }

  @Test
  public void writeNewInvoice() {
    Letter letter = new Letter();
    letter.setText("written text");

    dataStore.save(letter);

    assertTrue("The new letter was saved", dataStore.getLetters().contains(letter));
    Letter loadedLetter = dataStore.getLetter().orElse(null);
    assertEquals("The new letter contains the written text", "written text", loadedLetter.getText());
  }
}
