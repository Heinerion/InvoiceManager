package de.heinerion.betriebe.gui.panels;

import de.heinerion.betriebe.gui.TaschenrechnerPanel;

@SuppressWarnings("serial")
class CalculatorPanel extends SidePanel {
  public CalculatorPanel() {
    add(new TaschenrechnerPanel());
  }
}
