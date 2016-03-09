package de.heinerion.betriebe;

import de.heinerion.betriebe.classes.file_operations.IO;
import de.heinerion.betriebe.classes.gui.RechnungFrame;
import de.heinerion.betriebe.enums.Utilities;
import de.heinerion.betriebe.tools.gui.LookAndFeel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.swing.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RechnungFrame.class, LookAndFeel.class, IO.class})
@PowerMockIgnore("javax.management.*")
public class InvoiceManagerTest {
  @Mock
  private RechnungFrame frame;

  @Mock
  private JProgressBar progressBar;

  @Before
  public void setUp() {
    mockStatic(IO.class);

    mockStatic(RechnungFrame.class);
    when(RechnungFrame.getInstance()).thenReturn(frame);
    when(frame.getProgressBar()).thenReturn(progressBar);

    mockStatic(LookAndFeel.class);
  }

  @Test
  public void testMainSetDebugMode() {
    InvoiceManager.main("debug");

    assertTrue(Utilities.isDebugMode());
  }

  @Test
  public void testMainNotSetDebugMode() {
    InvoiceManager.main();

    assertFalse(Utilities.isDebugMode());
  }

  @Test
  public void testMainSetFrameVisible() {
    InvoiceManager.main("debug");

    verify(frame).setVisible(true);
  }

  @Test
  public void testMainSetLookAndFeelNimbus() {
    PowerMockito.doCallRealMethod().when(LookAndFeel.class);
    LookAndFeel.setNimbus();

    InvoiceManager.main();

    ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    verify(LookAndFeel.class);
    LookAndFeel.setLookName(argument.capture());
    assertEquals("Nimbus", argument.getValue());
  }
}