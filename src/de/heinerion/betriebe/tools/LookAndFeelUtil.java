package de.heinerion.betriebe.tools;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public final class LookAndFeelUtil {
  private LookAndFeelUtil() {
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

  public static void setLookByName(String name) {
    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
      if (name.equals(info.getName())) {
        setLook(info.getClassName());
        break;
      }
    }
  }

  public static void setNimbus() {
    setLookByName("Nimbus");
  }

  public static void setSystem() {
    setLook(UIManager.getSystemLookAndFeelClassName());
  }

}
