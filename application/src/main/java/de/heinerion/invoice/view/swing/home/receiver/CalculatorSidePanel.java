package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.view.swing.home.PanelHolder;

import javax.swing.*;

class CalculatorSidePanel implements PanelHolder {
  private SidePanel sidePanel;

  CalculatorSidePanel(Session session) {
    sidePanel = new SidePanel();
    sidePanel.add(new CalculatorPanel(session).getPanel());
  }

  @Override
  public JPanel getPanel() {
    return sidePanel;
  }
}
