package de.heinerion.betriebe.enums;

import java.io.File;

public enum Pfade {

  /** Pfad für den Vorlagen<b>ordner</b> für spezielle Rechnungen */
  VORLAGENSPEZIAL("vorlagenSpezial"),
  VORLAGEN("Vorlagen"),
  /** Pfad für die Performance CSV */
  CSV("performance.csv"),
  /** Pfad für die Logfile */
  LOG("logfile.txt"),
  /** Pfad für LogFile Datum */
  LOG_DATE("logdate.sav");

  private String name;
  private String path;

  Pfade(String aName) {
    this(aName, Utilities.SYSTEM + File.separator + aName);
  }

  Pfade(String aName, String aPath) {
    name = aName;
    path = aPath;
  }

  public String file(String fileBaseName) {
    return getPath() + File.separator + fileBaseName + ".sav";
  }

  public String getName() {
    return this.name;
  }

  public String getPath() {
    return this.path;
  }

  @Override
  public String toString() {
    return this.path;
  }
}
