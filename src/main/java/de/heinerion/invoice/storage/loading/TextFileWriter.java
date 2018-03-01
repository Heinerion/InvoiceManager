package de.heinerion.invoice.storage.loading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class TextFileWriter implements Writer {
  private static final String PROPERTY_DEVIDER = ":";
  private static final String LINE_END = "\r\n";

  private java.io.Writer writer = null;
  private boolean isOpen = false;

  @Override
  public void closeFile() throws IOException {
    if (this.isOpen) {
      this.writer.close();
    }
  }

  @Override
  public void prepareFile(String path) throws IOException {
    final String parentPath = path.substring(0,
        path.lastIndexOf(File.separator));
    final String filePath = path
        .substring(path.lastIndexOf(File.separator) + 1);

    final File parentFile = new File(parentPath);
    parentFile.mkdirs();
    final File file = new File(parentFile, filePath);

    this.writer = new BufferedWriter(new FileWriter(file));
    this.isOpen = true;
  }

  @Override
  public void write(String key, String value) throws IOException {
    this.writer.write(key + PROPERTY_DEVIDER + value + LINE_END);
  }

}
