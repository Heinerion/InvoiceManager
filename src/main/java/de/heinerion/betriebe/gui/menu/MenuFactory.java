package de.heinerion.betriebe.gui.menu;

import javax.swing.*;

public class MenuFactory {
  private MenuFactory() {
  }

  public static JMenuBar createMenuBar(JFrame frame) {
    return new MenuBar(frame);
  }
}
