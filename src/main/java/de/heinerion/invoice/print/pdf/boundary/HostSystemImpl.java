package de.heinerion.invoice.print.pdf.boundary;

import java.io.File;

class HostSystemImpl implements HostSystem {
  private final FileHandler fileHandler;
  private final SystemCall systemCall;

  HostSystemImpl(FileHandler fileHandler, SystemCall systemCall) {
    this.fileHandler = fileHandler;
    this.systemCall = systemCall;
  }

  @Override
  public void pdfLatex(File tex) {
    systemCall.pdfLatex(tex);
  }

  @Override
  public File writeToFile(String path, String content) {
    return fileHandler.writeToFile(path, content);
  }

  @Override
  public void moveFile(String destinationPath, File file) {
    fileHandler.moveFile(destinationPath, file);
  }

  @Override
  public void deleteFile(String filename) {
    fileHandler.deleteFile(filename);
  }
}
