package de.heinerion.invoice.view;

import de.heinerion.invoice.view.swing.home.ApplicationFrame;
import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GuiStarterTest {

  @Mock
  private JFrame frame;
  @Mock
  private ApplicationFrame applicationFrame;
  @Mock
  private LookAndFeelUtil lookAndFeel;

  @Test
  void showInterface_makesFrameVisible() {
    when(applicationFrame.getFrame()).thenReturn(frame);

    new GuiStarter(applicationFrame, lookAndFeel).showInterface();

    verify(applicationFrame).getFrame();
    verify(frame).setVisible(true);
    verify(frame).setLocationRelativeTo(null);
  }

  @Test
  void showInterface_appliesLookAndFeel() {
    when(applicationFrame.getFrame()).thenReturn(frame);

    new GuiStarter(applicationFrame, lookAndFeel).showInterface();

    verify(lookAndFeel).setLookAndFeel(applicationFrame);
  }
}