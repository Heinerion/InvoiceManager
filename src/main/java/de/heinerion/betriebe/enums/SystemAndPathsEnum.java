package de.heinerion.betriebe.enums;

import de.heinerion.betriebe.services.ConfigurationService;

import java.io.File;

public class SystemAndPathsEnum {
  private static boolean debugMode = false;

  private SystemAndPathsEnum() {
  }

  public static boolean isDebugMode() {
    return debugMode;
  }

  public static void setDebugMode(boolean isDebugMode) {
    SystemAndPathsEnum.debugMode = isDebugMode;
  }
}
