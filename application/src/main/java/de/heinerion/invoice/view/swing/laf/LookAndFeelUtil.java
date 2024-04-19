package de.heinerion.invoice.view.swing.laf;

import com.jthemedetecor.OsThemeDetector;
import de.heinerion.invoice.view.swing.home.ApplicationFrame;
import lombok.extern.flogger.Flogger;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.*;
import java.util.Optional;

@Flogger
public final class LookAndFeelUtil {
  private static boolean darkMode;

  private static final ThemeFactory themeFactory = new ThemeFactory();

  private LookAndFeelUtil() {
  }

  public static void setLookAndFeel(ApplicationFrame applicationFrame) {
    OsThemeDetector detector = OsThemeDetector.getDetector();
    setLookAndFeel(detector.isDark(), applicationFrame);
    detector.registerListener(isDark -> SwingUtilities.invokeLater(() -> setLookAndFeel(isDark, applicationFrame)));
  }

  private static void setLookAndFeel(boolean isDark, ApplicationFrame applicationFrame) {
    log.atFine().log("switch to %s mode", isDark ? "dark" : "light");
    darkMode = isDark;
    setLook(isDark
        ? themeFactory.getDarkTheme()
        : themeFactory.getLightTheme());

    SwingUtilities.updateComponentTreeUI(applicationFrame.getFrame());
    applicationFrame.getFrame().pack();
  }

  private static boolean isDarkMode() {
    return darkMode;
  }

  public static Color adjustColorByTheme(Color color) {
    return isDarkMode()
        ? color.darker()
        : color.brighter();
  }

  private static void setLook(String look) {
    try {
      UIManager.setLookAndFeel(look);
    } catch (ClassNotFoundException | InstantiationException
             | IllegalAccessException e) {
      // ignore
      setSystem();
    } catch (UnsupportedLookAndFeelException e) {
      logUnsupportedLookAndFeelException(e, look);
      setSystem();
    }
  }

  public static void setLook(BasicLookAndFeel lookAndFeel) {
    try {
      UIManager.setLookAndFeel(lookAndFeel);
    } catch (UnsupportedLookAndFeelException e) {
      logUnsupportedLookAndFeelException(e, deriveClassname(lookAndFeel));
      setNimbus();
    }
  }

  private static String deriveClassname(BasicLookAndFeel lookAndFeel) {
    return Optional.ofNullable(lookAndFeel)
        .map(Object::getClass)
        .map(Class::getName)
        .orElse("unknown");
  }

  private static void logUnsupportedLookAndFeelException(UnsupportedLookAndFeelException e, String name) {
    log.atSevere()
        .withCause(e)
        .log("Look and Feel not set.Class name is %s", name);
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

  private static void setSystem() {
    setLook(UIManager.getSystemLookAndFeelClassName());
  }
}
