package de.heinerion.betriebe.classes.gui;

import de.heinerion.betriebe.tools.math.Mathe;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BGPanel extends JPanel {
  // Primzahlen => Produkt immer eindeutig
  public static final int OBEN = 2;
  public static final int UNTEN = 3;
  public static final int LINKS = 5;
  public static final int RECHTS = 7;

  private static final int WIDTH = 15;

  private static final long serialVersionUID = 1L;

  private boolean oben;
  private boolean unten;
  private boolean rechts;
  private boolean links;

  private int widthOben;
  private int widthLinks;
  private int widthUnten;
  private int widthRechts;

  private Color rand;
  private Color zentrum;
  private Color[] farben;
  private float[] fraktionen = new float[]{0f, 1f};

  private RadialGradientPaint ol;
  private RadialGradientPaint or;
  private RadialGradientPaint ul;
  private RadialGradientPaint ur;

  private GradientPaint gpWeiss;
  private GradientPaint gpLinks;
  private GradientPaint gpRechts;
  private GradientPaint gpOben;
  private GradientPaint gpUnten;

  public BGPanel(int... sides) {
    int direction = Mathe.produkt(sides);
    oben = direction % OBEN == 0;
    unten = direction % UNTEN == 0;
    links = direction % LINKS == 0;
    rechts = direction % RECHTS == 0;

    widthOben = (oben) ? WIDTH : 0;
    widthLinks = (links) ? WIDTH : 0;
    widthUnten = (unten) ? WIDTH : 0;
    widthRechts = (rechts) ? WIDTH : 0;

    setBorder(BorderFactory.createEmptyBorder(widthOben, widthLinks, widthUnten, widthRechts));

    rand = (new JPanel()).getBackground();
    zentrum = Color.WHITE;
    farben = new Color[]{zentrum, rand};
  }

  private void drawAreas(Graphics2D g2, int x, int y) {
    g2.setPaint(links ? gpLinks : gpWeiss);
    g2.fill(new Rectangle2D.Double(x, widthOben, getWidth() / 2, getHeight() - widthUnten - widthOben));

    g2.setPaint(rechts ? gpRechts : gpWeiss);
    g2.fill(new Rectangle2D.Double(getWidth() / 2, widthOben, getWidth(), getHeight() - widthUnten - widthOben));

    g2.setPaint(oben ? gpOben : gpWeiss);
    g2.fill(new Rectangle2D.Double(widthLinks, y, getWidth() - widthRechts - widthLinks, getHeight() / 2));

    g2.setPaint(unten ? gpUnten : gpWeiss);
    g2.fill(new Rectangle2D.Double(widthLinks, getHeight() / 2, getWidth() - widthRechts - widthLinks, getHeight()));
  }

  private void drawCorners(Graphics2D g2, int x, int y) {
    if (oben && links) {
      g2.setPaint(ol);
      g2.fillRect(x, y, WIDTH, WIDTH);
    }
    if (oben && rechts) {
      g2.setPaint(or);
      g2.fillRect(getWidth() - WIDTH, y, WIDTH, WIDTH);
    }
    if (unten && links) {
      g2.setPaint(ul);
      g2.fillRect(x, getHeight() - WIDTH, WIDTH, WIDTH);
    }
    if (unten && rechts) {
      g2.setPaint(ur);
      g2.fillRect(getWidth() - WIDTH, getHeight() - WIDTH, WIDTH, WIDTH);
    }
  }

  private void initColors(int activeWidth, int activeHeight) {
    ol = new RadialGradientPaint(WIDTH, WIDTH, WIDTH, fraktionen, farben);
    or = new RadialGradientPaint(activeWidth, WIDTH, WIDTH, fraktionen, farben);
    ul = new RadialGradientPaint(WIDTH, activeHeight, WIDTH, fraktionen, farben);
    ur = new RadialGradientPaint(activeWidth, activeHeight, WIDTH, fraktionen, farben);
  }

  private void initPaint(int activeWidth, int activeHeight, int x, int y) {
    gpWeiss = new GradientPaint(x, y, zentrum, WIDTH, y, zentrum);
    gpLinks = new GradientPaint(x, y, rand, WIDTH, y, zentrum);
    gpRechts = new GradientPaint(activeWidth, y, zentrum, getWidth(), y, rand);
    gpOben = new GradientPaint(x, y, rand, x, WIDTH, zentrum);
    gpUnten = new GradientPaint(x, activeHeight, zentrum, x, getHeight(), rand);
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

    int x = 0;
    int y = 0;

    int activeWidth = getWidth() - WIDTH;
    int activeHeight = getHeight() - WIDTH;

    initColors(activeWidth, activeHeight);
    initPaint(activeWidth, activeHeight, x, y);

    drawCorners(g2, x, y);
    drawAreas(g2, x, y);
  }
}
