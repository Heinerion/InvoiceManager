package de.heinerion.betriebe.classes.gui.panels;

import de.heinerion.betriebe.gui.panels.AddressPanel;
import de.heinerion.betriebe.gui.panels.CalculatorPanel;
import de.heinerion.betriebe.gui.panels.CompanyChooserPanel;
import de.heinerion.betriebe.gui.panels.PrintButtonPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public final class ReceiverPanel extends JPanel {

  private AddressPanel addressPanel;
  private CompanyChooserPanel companyChooserPanel;

  public ReceiverPanel() {
    createWidgets();
    addWidgets();
  }

  private void createWidgets() {
    addressPanel = new AddressPanel();
    companyChooserPanel = new CompanyChooserPanel();
  }

  private void addWidgets() {
    setOpaque(false);
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    add(companyChooserPanel);
    add(addressPanel);

    add(new PrintButtonPanel());

    add(Box.createVerticalGlue());
    add(new CalculatorPanel());
  }

  // TODO Mit Address-Listener funktionalität ersetzen...
  public void refreshBoxes() {
    addressPanel.refreshBoxes();
    companyChooserPanel.refresh();
  }

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
    // Tabbereich
    g2.fill(new Rectangle2D.Double(x, y, getWidth(), getHeight()));
  }

  private Graphics2D initGraphic(Graphics2D g) {
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    return g;
  }
}