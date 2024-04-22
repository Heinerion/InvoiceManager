package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.view.swing.home.PanelHolder;
import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;

import javax.swing.*;

class CalculatorSidePanel implements PanelHolder {
  private final SidePanel sidePanel;

  CalculatorSidePanel(Session session, LookAndFeelUtil lookAndFeelUtil) {
    sidePanel = new SidePanel();
    sidePanel.add(new CalculatorPanel(session, lookAndFeelUtil).getPanel());
  }

  @Override
  public JPanel getPanel() {
    return sidePanel;
  }
}
