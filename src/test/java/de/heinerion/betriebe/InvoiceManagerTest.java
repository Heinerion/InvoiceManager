package de.heinerion.betriebe;

import de.heinerion.betriebe.builder.SessionPreparer;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.loading.IO;
import de.heinerion.betriebe.view.common.StatusComponent;
import de.heinerion.betriebe.view.swing.ApplicationFrame;
import de.heinerion.betriebe.view.swing.PanelFactory;
import de.heinerion.betriebe.view.swing.PanelSides;
import de.heinerion.betriebe.view.swing.SwingStarter;
import de.heinerion.util.LookAndFeelUtil;
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
public class InvoiceManagerTest {
  @Mock
  private JFrame frame;

  @Mock
  private ApplicationFrame applicationFrame;

  @Mock
  private IO io;

  @Mock
  private StatusComponent<JPanel> statusComponent;

  @InjectMocks
  private SwingStarter swingStarter;

  private InvoiceManager manager;

  @Before
  public void setUp() {
    manager = new InvoiceManager(io, swingStarter);

    when(statusComponent.getContainer()).thenReturn(new JPanel());

    mockStatic(PanelFactory.class);
    when(PanelFactory.createBackgroundPanel(Matchers.<PanelSides>anyVararg())).thenReturn(new JPanel());
    when(PanelFactory.createStatusComponent()).thenReturn(statusComponent);

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

    InvoiceManager.main();

    ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    verify(LookAndFeelUtil.class);
    LookAndFeelUtil.setLookByName(argument.capture());
    assertEquals("Nimbus", argument.getValue());
  }
}