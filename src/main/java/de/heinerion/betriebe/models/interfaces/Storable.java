package de.heinerion.betriebe.models.interfaces;

import de.heinerion.betriebe.tools.PathTools;

public interface Storable {
  default String[] getClassification() {
    return new String[]{PathTools.determineFolderName(getClass())};
  }

  String getEntryName();

  default String getIdentification() {
    return PathTools.determineFileEnding(getClass());
  }
}
