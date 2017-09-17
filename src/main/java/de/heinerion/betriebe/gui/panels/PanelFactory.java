package de.heinerion.betriebe.gui.panels;

import javax.swing.*;

public class PanelFactory {
  private static ApplicationFrame instance;

  private PanelFactory() {
  }

  public static JPanel createBackgroundPanel(PanelSides... coloredSides) {
    return BGPanel.createBackgroundPanel(coloredSides);
  }

  static Refreshable createReveiverPanel() {
    return new ReceiverPanel();
  }

  private static ContentTabPane createTabPane() {
    return ContentTabPaneImpl.getInstance();
  }

  private static JPanel createGlassPane() {
    return new GlassPane();
  }

  public static ApplicationFrame createApplicationFrame() {
    if (null == instance) {
      instance = createApplicationFrame(new JFrame(), createGlassPane(), new JProgressBar(), createTabPane());
    }
    return instance;
  }

  private static ApplicationFrame createApplicationFrame(JFrame jFrame, JPanel glassPane, JProgressBar jProgressBar, ContentTabPane tabPane) {
    return new ApplicationFrameImpl(jFrame, glassPane, jProgressBar, tabPane);
  }

  public static ApplicationFrame createApplicationFrame(JFrame jFrame, JProgressBar jProgressBar) {
    return createApplicationFrame(jFrame, createGlassPane(), jProgressBar, createTabPane());
  }

  static void createApplicationFrame(JFrame jFrame) {
    createApplicationFrame(jFrame, createGlassPane(), new JProgressBar(), createTabPane());
  }
}
