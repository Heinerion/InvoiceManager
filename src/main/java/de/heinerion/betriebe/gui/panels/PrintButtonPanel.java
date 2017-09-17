package de.heinerion.betriebe.gui.panels;

import de.heinerion.betriebe.listener.PrintAction;

import javax.swing.*;

@SuppressWarnings("serial")
class PrintButtonPanel extends SidePanel {
  PrintButtonPanel() {
    final JButton btnDrucken = new JButton("Drucken");
    btnDrucken.setName("print");
    add(btnDrucken);
    btnDrucken.addActionListener(new PrintAction());
  }
}
