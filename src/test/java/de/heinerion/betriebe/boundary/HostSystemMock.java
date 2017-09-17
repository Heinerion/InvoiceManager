package de.heinerion.betriebe.boundary;

import de.heinerion.betriebe.TestContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

class HostSystemMock implements HostSystem {
  @Autowired
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
