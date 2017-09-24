package de.heinerion.betriebe.view.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
class ReceiverPanel implements Refreshable {
  private JPanel panel;

  private Refreshable addressPanel;
  private Refreshable companyChooserPanel;

  ReceiverPanel() {
    createWidgets();
    addWidgets();
  }

  private void createWidgets() {
    panel = new FancyPanel();
    addressPanel = SidePanelFactory.createAddressPanel();
    companyChooserPanel = SidePanelFactory.createCompanyChooserPanel();
  }

  private void addWidgets() {
    panel.setOpaque(false);
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

    panel.add(companyChooserPanel.getPanel());
    panel.add(addressPanel.getPanel());

    panel.add(SidePanelFactory.createPrintButtonPanel());

    panel.add(Box.createVerticalGlue());
    panel.add(SidePanelFactory.createCalculatorSidePanel());
  }

  @Override
  public JPanel getPanel() {
    return panel;
  }

  // TODO Mit Address-Listener funktionalität ersetzen...
  @Override
  public void refresh() {
    companyChooserPanel.refresh();
    addressPanel.refresh();
  }

  private class FancyPanel extends JPanel {
    @Override
    public void paintComponent(final Graphics g) {
      Graphics2D g2 = initGraphic((Graphics2D) g);

      g2.setPaint(Color.gray);

      Color decor = getBackground().darker();
      Color base = getBackground();

      int decorWidth = 10;
      int x = 0;
      int y = 0;

      GradientPaint baseToDecorGradient = new GradientPaint(x, y, decor, decorWidth,
          y, base);
      g2.setPaint(baseToDecorGradient);
      // Tab area
      g2.fill(new Rectangle2D.Double(x, y, getWidth(), getHeight()));
    }

    private Graphics2D initGraphic(Graphics2D g) {
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      return g;
    }
  }
}