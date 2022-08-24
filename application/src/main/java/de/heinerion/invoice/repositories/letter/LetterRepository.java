package de.heinerion.invoice.repositories.letter;

import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.models.Letter;
import de.heinerion.invoice.repositories.Repository;

import java.util.Collection;

public interface LetterRepository extends Repository<Letter> {
  Collection<Letter> findAllBySender(Company sender);
}
