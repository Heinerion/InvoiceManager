package de.heinerion.betriebe.boundary;

import de.heinerion.betriebe.TestContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class HostSystemMock extends HostSystem {
  @Autowired
  public HostSystemMock() {
    super(null, null);
  }

  public void pdfLatex(File tex) {
    TestContext.addMessage("pdflatex \"" + tex + "\"");
  }

  public File writeToFile(String path, String content) {
    return new File("path");
  }

  public void moveFile(String destinationPath, File file) {
    TestContext.addMessage("moveFile(\"" + destinationPath + "\", \"" + file + "\")");
  }

  public void deleteFile(String filename) {
    TestContext.addMessage("deleteFile(\"" + filename + "\")");
  }
}
