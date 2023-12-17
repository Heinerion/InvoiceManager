package de.heinerion.invoice.view.swing.laf;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.*;
import com.formdev.flatlaf.util.SystemInfo;
import lombok.Getter;

import javax.swing.plaf.basic.BasicLookAndFeel;

@Getter
final class ThemeFactory {

  private final BasicLookAndFeel lightTheme;
  private final BasicLookAndFeel darkTheme;

  public ThemeFactory() {
    if (SystemInfo.isMacOS) {
      this.lightTheme = new FlatMacLightLaf();
      this.darkTheme = new FlatMacDarkLaf();
    } else {
      System.setProperty("flatlaf.menuBarEmbedded", "false");
      this.lightTheme = new FlatLightLaf();
      this.darkTheme = new FlatDarkLaf();
    }
  }
}
