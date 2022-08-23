package de.heinerion.betriebe.repositories.letter;

import de.heinerion.betriebe.models.Letter;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

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