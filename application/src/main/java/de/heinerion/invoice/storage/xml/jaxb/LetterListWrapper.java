package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.models.Letter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "letters")
class LetterListWrapper {
  private List<Letter> letters;

  @XmlElement(name = "letters")
  public List<Letter> getLetters() {
    return letters;
  }

  public void setLetters(List<Letter> letters) {
    this.letters = letters;
  }
}