package de.heinerion.invoice.print.pdf.boundary;

import de.heinerion.invoice.boundary.HostSystem;

import java.io.File;

class HostSystemMock implements HostSystem {
  public HostSystemMock() {
  }

  @Override
  public void pdfLatex(File tex) {
    TestContext.addMessage("pdflatex \"" + tex + "\"");
  }

  @Override
  public File writeToFile(String path, String content) {
    return new File("path");
  }

  @Override
  public void moveFile(String destinationPath, File file) {
    TestContext.addMessage("moveFile(\"" + destinationPath + "\", \"" + file + "\")");
  }

  @Override
  public void deleteFile(String filename) {
    TestContext.addMessage("deleteFile(\"" + filename + "\")");
  }
}
