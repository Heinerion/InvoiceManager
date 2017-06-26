package de.heinerion.betriebe.data.listable;

import de.heinerion.betriebe.tools.PathUtil;

import java.io.File;
import java.io.Serializable;

// TODO zu RawTemplate machen, Coupling reduzieren
@SuppressWarnings("serial")
public final class TexTemplate implements DropListable, Serializable {

  private String name;

  private final String path;

  public TexTemplate(String aName) {
    this.name = aName.split("\\.", 2)[0];
    this.path = PathUtil.getTexTemplatePath() + File.separator + aName;
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
