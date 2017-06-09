package de.heinerion.betriebe.gui.panels;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class SidePanel extends JPanel {
  public SidePanel() {
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
