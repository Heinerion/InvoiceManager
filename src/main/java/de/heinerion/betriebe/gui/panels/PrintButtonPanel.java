package de.heinerion.betriebe.gui.panels;

import javax.swing.JButton;

import de.heinerion.betriebe.listener.DruckAction;

@SuppressWarnings("serial")
public class PrintButtonPanel extends SidePanel {
  public PrintButtonPanel() {
    final JButton btnDrucken = new JButton("Drucken");
    add(btnDrucken);
    btnDrucken.addActionListener(new DruckAction());
  }
}
