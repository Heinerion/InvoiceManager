package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.util.PathUtilNG;

import javax.swing.*;

public class MenuFactory {
  private MenuFactory() {
  }

  public static JMenuBar createMenuBar(JFrame frame, PathUtilNG pathUtil) {
    return new MenuBar(frame, pathUtil);
  }
}
