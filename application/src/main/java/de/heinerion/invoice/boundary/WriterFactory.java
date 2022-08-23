package de.heinerion.invoice.boundary;

import java.io.*;

public class WriterFactory {
  public Writer create(File file) throws IOException {
    return new BufferedWriter(new FileWriter(file));
  }
}
