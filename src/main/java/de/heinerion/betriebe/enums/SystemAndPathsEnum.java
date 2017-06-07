package de.heinerion.betriebe.enums;

import de.heinerion.betriebe.services.ConfigurationService;

import java.io.File;

public enum SystemAndPathsEnum {
  /**
   * Rechnung - Ordnername und Briefbetreff
   */
  RECHNUNG("Rechnung"),
  /**
   * Brief - Ordnername
   */
  BRIEF("Brief"),
  /**
   * System - Ordnername
   */
  SYSTEM("System"),
  VORLAGE("Vorlagen");

  private static boolean debugMode = false;

  private String text;

  SystemAndPathsEnum(String aText) {
    this.text = aText;
  }

  public static String applicationHome() {
    return System.getProperty("user.home") + File.separator + ConfigurationService.get("folder.data")
        + File.separator;
  }

  public static boolean isDebugMode() {
    return debugMode;
  }

  public static void setDebugMode(boolean isDebugMode) {
    SystemAndPathsEnum.debugMode = isDebugMode;
  }

  public String getPath() {
    return applicationHome() + this.text
        + (debugMode ? File.separator + "Debug" : "");
  }

  public String getText() {
    return this.text;
  }

  @Override
  public String toString() {
    return this.getPath();
  }
}
