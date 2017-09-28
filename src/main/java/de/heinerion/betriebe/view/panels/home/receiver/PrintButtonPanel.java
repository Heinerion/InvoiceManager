package de.heinerion.betriebe.view.panels.home.receiver;

import de.heinerion.betriebe.listener.PrintAction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

@SuppressWarnings("serial")
class PrintButtonPanel extends SidePanel {
  @Autowired
  PrintButtonPanel(PrintAction printAction) {
    final JButton btnDrucken = new JButton("Drucken");
    btnDrucken.setName("print");
    add(btnDrucken);
    btnDrucken.addActionListener(printAction);
  }
}
