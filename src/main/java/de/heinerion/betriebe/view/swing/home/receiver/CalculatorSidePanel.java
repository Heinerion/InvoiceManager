package de.heinerion.betriebe.view.swing.home.receiver;

import de.heinerion.betriebe.view.swing.home.PanelHolder;

import javax.swing.*;

@SuppressWarnings("serial")
class CalculatorSidePanel implements PanelHolder {
  private SidePanel sidePanel;

  CalculatorSidePanel() {
    sidePanel = new SidePanel();
    sidePanel.add(new CalculatorPanel().getPanel());
  }

  @Override
  public JPanel getPanel() {
    return sidePanel;
  }
}
