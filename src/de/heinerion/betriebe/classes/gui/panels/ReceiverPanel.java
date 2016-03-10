package de.heinerion.betriebe.classes.gui.panels;

import de.heinerion.betriebe.classes.gui.ApplicationFrame;
import de.heinerion.betriebe.gui.panels.AddressPanel;
import de.heinerion.betriebe.gui.panels.CalculatorPanel;
import de.heinerion.betriebe.gui.panels.CompanyChooserPanel;
import de.heinerion.betriebe.gui.panels.PrintButtonPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public final class ReceiverPanel extends JPanel {

  private final ApplicationFrame rechnungsFrame;
  private AddressPanel addressPanel;
  private CompanyChooserPanel companyChooserPanel;

  public ReceiverPanel(ApplicationFrame applicationFrame) {
    this.addressPanel = new AddressPanel();
    this.rechnungsFrame = applicationFrame;

    setOpaque(false);
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    companyChooserPanel = new CompanyChooserPanel();
    add(companyChooserPanel);
    add(addressPanel);

    add(new PrintButtonPanel());

    add(Box.createVerticalGlue());
    add(new CalculatorPanel());
  }

  public void refresh() {
    this.rechnungsFrame.refresh();
  }

  // TODO Mit Address-Listener funktionalität ersetzen...
  public void refreshBoxes() {
    addressPanel.refreshBoxes();
    companyChooserPanel.refresh();
  }

  @Override
  public void paintComponent(Graphics g) {
    final Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setPaint(Color.gray);

    int x = 0;
    int width = 10;
    int y = 0;

    final Color bright = this.getBackground().darker();
    final Color dark = this.getBackground();
    // TODO Hintergrund malen
    final GradientPaint whiteToBack = new GradientPaint(x, y, bright, width,
        y, dark);
    g2.setPaint(whiteToBack);
    // Tabbereich
    g2.fill(new Rectangle2D.Double(x, y, this.getWidth(), this.getHeight()));
  }
}