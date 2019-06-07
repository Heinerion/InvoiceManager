package de.heinerion.invoice.storage.loading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
  private static final String PROPERTY_DIVIDER = ":";
  private static final String LINE_END = "\r\n";

  private java.io.Writer writer = null;
  private boolean isOpen = false;

  void closeFile() throws IOException {
    if (isOpen) {
      writer.close();
    }
  }

  void prepareFile(String path) throws IOException {
    final String parentPath = path.substring(0,
        path.lastIndexOf(File.separator));
    final String filePath = path
        .substring(path.lastIndexOf(File.separator) + 1);

    final File parentFile = new File(parentPath);
    parentFile.mkdirs();
    final File file = new File(parentFile, filePath);

    writer = new BufferedWriter(new FileWriter(file));
    isOpen = true;
  }

  public void write(String key, String value) throws IOException {
    writer.write(key + PROPERTY_DIVIDER + value + LINE_END);
  }
}
