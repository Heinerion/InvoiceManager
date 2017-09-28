package de.heinerion.betriebe.view.panels.home.receiver;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
class SidePanel extends JPanel {
  SidePanel() {
    setOpaque(false);
    setLayout(new FlowLayout());
  }

  @Override
  public final Component add(Component comp) {
    final Component result = super.add(comp);

    setMaximumSize(comp.getPreferredSize());
    return result;
  }
}
