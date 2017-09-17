package de.heinerion.betriebe.boundary;

import java.io.File;

public interface HostSystem {
  void pdfLatex(File tex);

  File writeToFile(String path, String content);

  void moveFile(String destinationPath, File file);

  void deleteFile(String filename);
}
