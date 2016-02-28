package de.heinerion.betriebe.tools.gui;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public final class LookAndFeel {
  private LookAndFeel() {
  }

  public static String getClassName() {
    return UIManager.getLookAndFeel().getClass().getName();
  }

  public static String getName() {
    return UIManager.getLookAndFeel().getName();
  }

  protected static void setLook(String look) {
    try {
      UIManager.setLookAndFeel(look);
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
      setSystem();
    }
  }

  public static void setLookName(String name) {
    for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
      if (name.equals(info.getName())) {
        setLook(info.getClassName());
        break;
      }
    }
  }

  public static void setNimbus() {
    setLookName("Nimbus");
  }

  public static void setSystem() {
    setLook(UIManager.getSystemLookAndFeelClassName());
  }

}
