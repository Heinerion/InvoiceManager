package de.heinerion.invoice.view.swing.home;

import de.heinerion.invoice.view.swing.ApplicationFrame;

import javax.swing.*;

public class ApplicationFrameDummy implements ApplicationFrame {
  @Override
  public JFrame getFrame() {
    return null;
  }

  @Override
  public void refresh() {
  }
}
