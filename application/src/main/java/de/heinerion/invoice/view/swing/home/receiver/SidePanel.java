package de.heinerion.invoice.view.swing.home.receiver;

import javax.swing.*;
import java.awt.*;

final class SidePanel extends JPanel {
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
