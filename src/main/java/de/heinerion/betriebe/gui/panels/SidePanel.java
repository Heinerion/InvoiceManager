package de.heinerion.betriebe.gui.panels;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JPanel;

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
