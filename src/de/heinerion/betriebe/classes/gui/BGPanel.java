package de.heinerion.betriebe.classes.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import de.heinerion.betriebe.tools.math.Mathe;

public class BGPanel extends JPanel {
  // Primzahlen => Produkt immer eindeutig
  public static final int OBEN = 2;
  public static final int UNTEN = 3;
  public static final int LINKS = 5;
  public static final int RECHTS = 7;

  private static final int WIDTH = 15;

  private static final long serialVersionUID = 1L;

  private final boolean oben;
  private final boolean unten;
  private final boolean rechts;
  private final boolean links;

  private final int widthOben;
  private final int widthLinks;
  private final int widthUnten;
  private final int widthRechts;

  private final Color rand;
  private final Color zentrum;
  private final Color[] farben;
  private final float[] fraktionen = new float[] { 0f, 1f };

  private RadialGradientPaint ol;
  private RadialGradientPaint or;
  private RadialGradientPaint ul;
  private RadialGradientPaint ur;

  private GradientPaint gpWeiss;
  private GradientPaint gpLinks;
  private GradientPaint gpRechts;
  private GradientPaint gpOben;
  private GradientPaint gpUnten;

  public BGPanel(int... seiten) {
    final int direction = Mathe.produkt(seiten);
    this.oben = direction % OBEN == 0;
    this.unten = direction % UNTEN == 0;
    this.links = direction % LINKS == 0;
    this.rechts = direction % RECHTS == 0;

    this.widthOben = (this.oben) ? WIDTH : 0;
    this.widthLinks = (this.links) ? WIDTH : 0;
    this.widthUnten = (this.unten) ? WIDTH : 0;
    this.widthRechts = (this.rechts) ? WIDTH : 0;

    this.setBorder(BorderFactory.createEmptyBorder(this.widthOben,
        this.widthLinks, this.widthUnten, this.widthRechts));

    this.rand = (new JPanel()).getBackground();
    this.zentrum = Color.WHITE;
    this.farben = new Color[] { this.zentrum, this.rand };
  }

  /**
   * @param g2
   * @param x
   * @param y
   */
  private void drawAreas(final Graphics2D g2, final int x, final int y) {
    g2.setPaint(this.links ? this.gpLinks : this.gpWeiss);
    g2.fill(new Rectangle2D.Double(x, this.widthOben, this.getWidth() / 2, this
        .getHeight() - this.widthUnten - this.widthOben));

    g2.setPaint(this.rechts ? this.gpRechts : this.gpWeiss);
    g2.fill(new Rectangle2D.Double(this.getWidth() / 2, this.widthOben, this
        .getWidth(), this.getHeight() - this.widthUnten - this.widthOben));

    g2.setPaint(this.oben ? this.gpOben : this.gpWeiss);
    g2.fill(new Rectangle2D.Double(this.widthLinks, y, this.getWidth()
        - this.widthRechts - this.widthLinks, this.getHeight() / 2));

    g2.setPaint(this.unten ? this.gpUnten : this.gpWeiss);
    g2.fill(new Rectangle2D.Double(this.widthLinks, this.getHeight() / 2, this
        .getWidth() - this.widthRechts - this.widthLinks, this.getHeight()));
  }

  /**
   * @param g2
   * @param x
   * @param y
   */
  private void drawCorners(final Graphics2D g2, final int x, final int y) {
    if (this.oben && this.links) {
      g2.setPaint(this.ol);
      g2.fillRect(x, y, WIDTH, WIDTH);
    }
    if (this.oben && this.rechts) {
      g2.setPaint(this.or);
      g2.fillRect(this.getWidth() - WIDTH, y, WIDTH, WIDTH);
    }
    if (this.unten && this.links) {
      g2.setPaint(this.ul);
      g2.fillRect(x, this.getHeight() - WIDTH, WIDTH, WIDTH);
    }
    if (this.unten && this.rechts) {
      g2.setPaint(this.ur);
      g2.fillRect(this.getWidth() - WIDTH, this.getHeight() - WIDTH, WIDTH,
          WIDTH);
    }
  }

  private void initColors(int activeWidth, int activeHeight) {
    this.ol = new RadialGradientPaint(WIDTH, WIDTH, WIDTH, this.fraktionen,
        this.farben);
    this.or = new RadialGradientPaint(activeWidth, WIDTH, WIDTH,
        this.fraktionen, this.farben);
    this.ul = new RadialGradientPaint(WIDTH, activeHeight, WIDTH,
        this.fraktionen, this.farben);
    this.ur = new RadialGradientPaint(activeWidth, activeHeight, WIDTH,
        this.fraktionen, this.farben);
  }

  private void initPaint(int activeWidth, int activeHeight, int x, int y) {
    this.gpWeiss = new GradientPaint(x, y, this.zentrum, WIDTH, y, this.zentrum);
    this.gpLinks = new GradientPaint(x, y, this.rand, WIDTH, y, this.zentrum);
    this.gpRechts = new GradientPaint(activeWidth, y, this.zentrum,
        this.getWidth(), y, this.rand);
    this.gpOben = new GradientPaint(x, y, this.rand, x, WIDTH, this.zentrum);
    this.gpUnten = new GradientPaint(x, activeHeight, this.zentrum, x,
        this.getHeight(), this.rand);
  }

  @Override
  public final void paintComponent(Graphics g) {
    final Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    // Panel:
    //
    // []|......|[]
    // --+------+--
    // ..|......|..
    // ..|......|..
    // --+------+--
    // []|......|[]
    //
    // zwischen | und | -> Oben/Unten Fade
    // zwischen - und - -> Links/Rechts Fade [] : IF Anliegende Seiten
    // beide: Diagonal IF beide nicht: nichts IF eine ja eine nein: wie die
    // aktive

    final int x = 0;
    final int y = 0;

    final int activeWidth = this.getWidth() - WIDTH;
    final int activeHeight = this.getHeight() - WIDTH;

    this.initColors(activeWidth, activeHeight);
    this.initPaint(activeWidth, activeHeight, x, y);

    this.drawCorners(g2, x, y);
    this.drawAreas(g2, x, y);
  }
}
