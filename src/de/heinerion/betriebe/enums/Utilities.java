package de.heinerion.betriebe.enums;

import java.io.File;

public enum Utilities {
  /** Fenstertitel: führender text */
  TITEL("Betrieb: "),
  /** Fenstertitel: Rechnungsnummer angabe */
  NUMMER("Rechnungsnummer: "),

  /** Rechnung - Ordnername und Briefbetreff */
  RECHNUNG("Rechnung"),
  /** Brief - Ordnername */
  BRIEF("Brief"),
  /** System - Ordnername */
  SYSTEM("System"),
  VORLAGE("Vorlagen");

  private static boolean debugMode = false;

  private String text;

  Utilities(String aText) {
    this.text = aText;
  }

  public static String applicationHome() {
    return System.getProperty("user.home") + File.separator + "Betriebe"
        + File.separator;
  }

  public static boolean isDebugMode() {
    return debugMode;
  }

  public static void setDebugMode(boolean isDebugMode) {
    Utilities.debugMode = isDebugMode;
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
