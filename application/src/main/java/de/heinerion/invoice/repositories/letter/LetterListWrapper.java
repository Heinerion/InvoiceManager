package de.heinerion.invoice.repositories.letter;

import de.heinerion.invoice.models.Letter;
import jakarta.xml.bind.annotation.*;

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