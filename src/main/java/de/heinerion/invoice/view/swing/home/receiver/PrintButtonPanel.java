package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.listener.PrintAction;
import de.heinerion.invoice.view.swing.home.PanelHolder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

@SuppressWarnings("serial")
class PrintButtonPanel implements PanelHolder {
  private SidePanel sidePanel;

  @Autowired
  PrintButtonPanel(PrintAction printAction) {
    sidePanel = new SidePanel();

    final JButton btnDrucken = new JButton("Drucken");
    btnDrucken.setName("print");
    sidePanel.add(btnDrucken);
    btnDrucken.addActionListener(printAction);
  }

  @Override
  public JPanel getPanel() {
    return sidePanel;
  }
}
