package de.heinerion.betriebe.data.listable;

import java.io.File;
import java.io.Serializable;

// TODO zu RawTemplate machen, Coupling reduzieren
@SuppressWarnings("serial")
public final class TexTemplate implements DropListable, Serializable {

  private String name;

  private final String path;

  public TexTemplate(String basePath, String aName) {
    this.name = aName.split("\\.", 2)[0];
    this.path = basePath + File.separator + aName;
  }

  @Override
  public String getName() {
    return this.name;
  }

  public String getPath() {
    return this.path;
  }

  @Override
  public void setName(String aName) {
    this.name = aName;
  }
}
