package de.heinerion.invoice.view.swing.home;

import de.heinerion.invoice.view.swing.home.receiver.*;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;

@Service
public class ReceiverPanel implements Refreshable {
  private JPanel panel;

  private Refreshable addressPanel;
  private Refreshable companyChooserPanel;

  private final PrintButtonPanel printButtonPanel;
  private final SidePanelFactory sidePanelFactory;

  ReceiverPanel(PrintButtonPanel printButtonPanel, SidePanelFactory sidePanelFactory) {
    this.printButtonPanel = printButtonPanel;
    this.sidePanelFactory = sidePanelFactory;
    createWidgets();
    addWidgets();
  }

  private void createWidgets() {
    panel = new FancyPanel();
    addressPanel = sidePanelFactory.createAddressPanel();
    companyChooserPanel = sidePanelFactory.createCompanyChooserPanel();
  }

  private void addWidgets() {
    panel.setOpaque(false);
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

    panel.add(companyChooserPanel.getPanel());
    panel.add(addressPanel.getPanel());

    panel.add(printButtonPanel.getPanel());

    panel.add(Box.createVerticalGlue());
    panel.add(sidePanelFactory.createCalculatorSidePanel());
  }

  @Override
  public JPanel getPanel() {
    return panel;
  }

  // TODO replace with address listener functionality
  @Override
  public void refresh() {
    companyChooserPanel.refresh();
    addressPanel.refresh();
  }

  public static class FancyPanel extends JPanel {
    @Override
    public void paintComponent(final Graphics g) {
      ComponentPainter.paintComponent(
          (Graphics2D) g,
          this,
          ComponentPainter.Details.builder()
              .decorWidth(10)
              .build());
    }
  }
}
