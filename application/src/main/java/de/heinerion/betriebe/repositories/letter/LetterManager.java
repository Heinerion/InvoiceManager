package de.heinerion.betriebe.repositories.letter;

import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.repositories.JaxbManager;

import java.util.List;

public class LetterManager extends JaxbManager<Letter> {
  @Override
  protected Object getRootObject() {
    return new LetterListWrapper();
  }

  @Override
  protected void setContent(Object wrapper, List<Letter> items) {
    ((LetterListWrapper) wrapper).setLetters(items);
  }

  @Override
  protected List<Letter> getContent(Object rootObject) {
    return ((LetterListWrapper) rootObject).getLetters();
  }

  @Override
  protected Class<?> getWrapper() {
    return LetterListWrapper.class;
  }
}
