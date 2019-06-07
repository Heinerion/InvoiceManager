package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.print.PrintAction;
import de.heinerion.invoice.view.swing.home.PanelHolder;

import javax.swing.*;

public class PrintButtonPanel implements PanelHolder {
  private SidePanel sidePanel;

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
