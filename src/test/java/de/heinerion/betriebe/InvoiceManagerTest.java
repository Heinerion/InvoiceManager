package de.heinerion.betriebe;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.fileoperations.IO;
import de.heinerion.betriebe.gui.ApplicationFrame;
import de.heinerion.betriebe.util.LookAndFeelUtil;
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
@PrepareForTest({ApplicationFrame.class, LookAndFeelUtil.class, IO.class})
@PowerMockIgnore("javax.management.*")
public class InvoiceManagerTest {
  @Mock
  private ApplicationFrame frame;

  @Mock
  private JProgressBar progressBar;

  @Before
  public void setUp() {
    mockStatic(IO.class);

    mockStatic(ApplicationFrame.class);
    when(ApplicationFrame.getInstance()).thenReturn(frame);
    when(frame.getProgressBar()).thenReturn(progressBar);

    mockStatic(LookAndFeelUtil.class);
  }

  @Test
  public void testMainSetDebugMode() {
    InvoiceManager.main("debug");

    assertTrue(Session.isDebugMode());
  }

  @Test
  public void testMainNotSetDebugMode() {
    InvoiceManager.main();

    assertFalse(Session.isDebugMode());
  }

  @Test
  public void testMainSetFrameVisible() {
    InvoiceManager.main("debug");

    verify(frame).setVisible(true);
  }

  @Test
  public void testMainSetLookAndFeelNimbus() {
    PowerMockito.doCallRealMethod().when(LookAndFeelUtil.class);
    LookAndFeelUtil.setNimbus();

    InvoiceManager.main();

    ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    verify(LookAndFeelUtil.class);
    LookAndFeelUtil.setLookByName(argument.capture());
    assertEquals("Nimbus", argument.getValue());
  }
}