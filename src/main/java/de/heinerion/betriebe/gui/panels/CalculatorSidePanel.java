package de.heinerion.betriebe.gui.panels;

@SuppressWarnings("serial")
class CalculatorSidePanel extends SidePanel {
  CalculatorSidePanel() {
    add(SidePanelFactory.createCalculatorPanel().getPanel());
  }
}
