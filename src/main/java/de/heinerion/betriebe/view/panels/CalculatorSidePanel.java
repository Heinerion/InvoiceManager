package de.heinerion.betriebe.view.panels;

@SuppressWarnings("serial")
class CalculatorSidePanel extends SidePanel {
  CalculatorSidePanel() {
    add(SidePanelFactory.createCalculatorPanel().getPanel());
  }
}
