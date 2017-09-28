package de.heinerion.betriebe.view.panels.home.receiver;

import de.heinerion.betriebe.view.panels.home.PanelHolder;

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
