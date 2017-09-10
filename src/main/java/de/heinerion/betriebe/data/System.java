package de.heinerion.betriebe.data;

public class System {
  private static boolean debugMode = false;

  private System() {
  }

  public static boolean isDebugMode() {
    return debugMode;
  }

  public static void isDebugMode(boolean isDebugActivated) {
    System.debugMode = isDebugActivated;
  }
}
