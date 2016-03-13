package de.heinerion.betriebe.classes.data;

import de.heinerion.betriebe.classes.texting.DropListable;
import de.heinerion.betriebe.enums.Pfade;

import java.io.File;
import java.io.Serializable;

// TODO zu RawTemplate machen, Coupling reduzieren
@SuppressWarnings("serial")
public final class TexVorlage implements DropListable, Serializable {

  /**
   * Vorlagenbezeichnung
   */
  private String name;

  private final String path;

  public TexVorlage(String aName) {
    this.name = aName.split("\\.", 2)[0];
    this.path = Pfade.VORLAGENSPEZIAL + File.separator + aName;
  }

  /**
   * returns the name of this <code>Vorlage</code>
   *
   * @return Vorlagenname
   */
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
