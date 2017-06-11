package de.heinerion.betriebe.betriebe.gui;

import de.heinerion.betriebe.gui.ApplicationFrame;
import de.heinerion.betriebe.services.Translator;
import org.fest.swing.fixture.FrameFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;

public class ApplicationFrameIT {
  private static FrameFixture demo;

  @BeforeClass
  public static void setUp() {
    JFrame frame = ApplicationFrame.getInstance();
    frame.setVisible(true);
    demo = new FrameFixture(frame);
  }

  @AfterClass
  public static void tearDown() {
    demo.cleanUp();
  }

  @Test
  public void testPrintButtonText() {
    demo.button("print").requireText("Drucken");
  }

  @Test
  public void testCalculatorPanel() {
    demo.label("calculator.netLabel").requireText(Translator.translate("invoice.net"));
  }
}