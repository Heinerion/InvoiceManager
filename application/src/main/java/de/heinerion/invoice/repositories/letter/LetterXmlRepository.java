package de.heinerion.invoice.repositories.letter;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.XmlRepository;

import java.util.Collection;

public interface LetterXmlRepository extends XmlRepository<Letter> {
  Collection<Letter> findAllBySender(Company sender);
}
