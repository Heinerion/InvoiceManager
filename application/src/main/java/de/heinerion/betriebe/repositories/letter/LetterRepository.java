package de.heinerion.betriebe.repositories.letter;

import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.repositories.Repository;

import java.util.Collection;

public interface LetterRepository extends Repository<Letter> {
  Collection<Letter> findAllBySender(Company sender);
}
