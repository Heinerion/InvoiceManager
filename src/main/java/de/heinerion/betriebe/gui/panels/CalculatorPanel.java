package de.heinerion.betriebe.gui.panels;

import de.heinerion.betriebe.classes.gui.TaschenrechnerPanel;

@SuppressWarnings("serial")
public class CalculatorPanel extends SidePanel {
  public CalculatorPanel() {
    add(new TaschenrechnerPanel());
  }
}
