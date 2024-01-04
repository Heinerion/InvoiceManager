package de.heinerion.invoice;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.testsupport.builder.SessionPreparer;
import de.heinerion.invoice.view.GuiStarter;
import de.heinerion.invoice.view.swing.ApplicationFrame;
import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.*;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.swing.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LookAndFeelUtil.class})
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
    manager = new InvoiceManager(Session.getInstance(), guiStarter);

    mockStatic(LookAndFeelUtil.class);

    when(applicationFrame.getFrame()).thenReturn(frame);

    new SessionPreparer()
        .prepare();
  }

  @Test
  public void testMainSetDebugMode() {
    manager.invoke(new String[]{"debug"});

    assertTrue(Session.getInstance().isDebugMode());
  }

  @Test
  public void testMainNotSetDebugMode() {
    manager.invoke(new String[]{});

    assertFalse(Session.getInstance().isDebugMode());
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

    new InvoiceManager(Session.getInstance(), guiStarter).run();

    ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    verify(LookAndFeelUtil.class);
    LookAndFeelUtil.setLookByName(argument.capture());
    assertEquals("Nimbus", argument.getValue());
  }
}