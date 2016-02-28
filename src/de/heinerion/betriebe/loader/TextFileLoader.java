package de.heinerion.betriebe.loader;

@Deprecated
public class TextFileLoader extends AbstractLoader {
  public TextFileLoader() {
    this.setWriter(new TextFileWriter());
    this.setReader(new TextFileReader());
  }
}
