package de.heinerion.betriebe.repositories;

import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Letter;

import java.util.Collection;

public interface LetterRepository extends Repository<Letter> {
  Collection<Letter> findAllBySender(Company sender);
}
