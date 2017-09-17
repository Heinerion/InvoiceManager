package de.heinerion.betriebe;

import de.heinerion.betriebe.builder.SessionPreparer;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.loading.IO;
import de.heinerion.betriebe.gui.panels.ApplicationFrame;
import de.heinerion.betriebe.gui.panels.PanelFactory;
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
@PrepareForTest({PanelFactory.class, LookAndFeelUtil.class, IO.class})
@PowerMockIgnore({"javax.management.*", "javax.swing.*"})
public class InvoiceManagerTest {
  @Mock
  private JFrame frame;

  @Mock
  private JProgressBar progressBar;

  @Before
  public void setUp() {
    mockStatic(IO.class);

    ApplicationFrame applicationFrame = PanelFactory.createApplicationFrame(frame, progressBar);

    mockStatic(PanelFactory.class);
    when(PanelFactory.createApplicationFrame()).thenReturn(applicationFrame);

    mockStatic(LookAndFeelUtil.class);

    new SessionPreparer()
        .withApplicationFrame(applicationFrame)
        .prepare();
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

    verify(Session.getFrame()).setVisible(true);
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