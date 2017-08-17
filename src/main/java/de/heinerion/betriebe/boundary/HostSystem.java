package de.heinerion.betriebe.boundary;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class HostSystem {
  private final FileHandler fileHandler;
  private final SystemCall systemCall;

  @Autowired
  public HostSystem(FileHandler fileHandler, SystemCall systemCall) {
    this.fileHandler = fileHandler;
    this.systemCall = systemCall;
  }

  public void pdfLatex(File tex) {
    systemCall.pdfLatex(tex);
  }

  public File writeToFile(String path, String content) {
    return fileHandler.writeToFile(path, content);
  }

  public void moveFile(String destinationPath, File file) {
    fileHandler.moveFile(destinationPath, file);
  }
}
