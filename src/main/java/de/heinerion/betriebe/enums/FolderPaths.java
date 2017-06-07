package de.heinerion.betriebe.enums;

import java.io.File;

public enum FolderPaths {

  TEX_TEMPLATES("vorlagenSpezial"),
  TEMPLATES("Vorlagen");

  private String name;
  private String path;

  FolderPaths(String aName) {
    name = aName;
    path = SystemAndPathsEnum.SYSTEM + File.separator + aName;
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
