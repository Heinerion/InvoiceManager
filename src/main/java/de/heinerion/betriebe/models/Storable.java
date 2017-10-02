package de.heinerion.betriebe.models;

import de.heinerion.betriebe.util.PathTools;

public interface Storable {
  default String[] getClassification() {
    return new String[]{PathTools.determineFolderName(getClass())};
  }

  String getEntryName();

  default String getIdentification() {
    return PathTools.determineFileEnding(getClass());
  }
}
