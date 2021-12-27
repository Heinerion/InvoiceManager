package de.heinerion.invoice;

import de.heinerion.betriebe.data.Session;
import de.heinerion.invoice.testsupport.builder.SessionPreparer;
import de.heinerion.invoice.view.GuiStarter;
import de.heinerion.invoice.view.swing.ApplicationFrame;
import de.heinerion.invoice.view.swing.LookAndFeelUtil;
import de.heinerion.invoice.view.swing.PanelFactory;
import de.heinerion.invoice.view.swing.PanelSides;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
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
@PrepareForTest({PanelFactory.class, LookAndFeelUtil.class})
@PowerMockIgnore({"javax.management.*", "javax.swing.*"})
// Ignore as long powermock is needed but not fixed
@Ignore
public class InvoiceManagerTest {
  @Mock
  private JFrame frame;

  @Mock
  private ApplicationFrame applicationFrame;

  @InjectMocks
  private GuiStarter guiStarter;

  private InvoiceManager manager;

  @Before
  public void setUp() {
    manager = new InvoiceManager(guiStarter);

    mockStatic(PanelFactory.class);
    when(PanelFactory.createBackgroundPanel(Matchers.<PanelSides>anyVararg())).thenReturn(new JPanel());

    mockStatic(LookAndFeelUtil.class);

    when(applicationFrame.getFrame()).thenReturn(frame);

    new SessionPreparer()
        .prepare();
  }

  @Test
  public void testMainSetDebugMode() {
    manager.invoke(new String[]{"debug"});

    assertTrue(Session.isDebugMode());
  }

  @Test
  public void testMainNotSetDebugMode() {
    manager.invoke(new String[]{});

    assertFalse(Session.isDebugMode());
  }

  @Test
  public void testMainSetFrameVisible() {
    manager.invoke(new String[]{"debug"});

    verify(frame).setVisible(true);
  }

  @Test
  @Ignore
  public void testMainSetLookAndFeelNimbus() {
    PowerMockito.doCallRealMethod().when(LookAndFeelUtil.class);
    LookAndFeelUtil.setNimbus();

    new InvoiceManager(guiStarter).run();

    ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    verify(LookAndFeelUtil.class);
    LookAndFeelUtil.setLookByName(argument.capture());
    assertEquals("Nimbus", argument.getValue());
  }
}