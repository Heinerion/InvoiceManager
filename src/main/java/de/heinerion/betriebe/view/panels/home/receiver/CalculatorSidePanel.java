package de.heinerion.betriebe.view.panels.home.receiver;

@SuppressWarnings("serial")
class CalculatorSidePanel extends SidePanel {
  CalculatorSidePanel() {
    add(SidePanelFactory.createCalculatorPanel().getPanel());
  }
}
