package de.heinerion.betriebe.classes.gui;

import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class ApplicationFrameIT {
  private FrameFixture demo;

  @Before
  public void setUp() {
    JFrame frame = ApplicationFrame.getInstance();
    frame.setVisible(true);
    demo = new FrameFixture(frame);
  }

  @After
  public void tearDown() {

    demo.cleanUp();
  }

  @Test
  public void test() {

    demo.button("print").requireText("Drucken");
  }
}