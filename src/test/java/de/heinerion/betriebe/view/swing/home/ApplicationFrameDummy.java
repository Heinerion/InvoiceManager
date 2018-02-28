package de.heinerion.betriebe.view.swing.home;

import de.heinerion.betriebe.view.common.StatusComponent;
import de.heinerion.betriebe.view.swing.ApplicationFrame;

import javax.swing.*;

public class ApplicationFrameDummy implements ApplicationFrame {
  @Override
  public JFrame getFrame() {
    return null;
  }

  @Override
  public void refresh() {

  }

  @Override
  public StatusComponent<JPanel> getStatusComponent() {
    return null;
  }
}
