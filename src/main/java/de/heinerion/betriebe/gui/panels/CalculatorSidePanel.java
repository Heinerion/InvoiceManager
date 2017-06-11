package de.heinerion.betriebe.gui.panels;

import de.heinerion.betriebe.gui.CalculatorPanel;

@SuppressWarnings("serial")
class CalculatorSidePanel extends SidePanel {
  public CalculatorSidePanel() {
    add(new CalculatorPanel());
  }
}
