package de.heinerion.invoice.repositories.letter;

import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.models.Letter;
import de.heinerion.invoice.repositories.AbstractXmlRepository;
import de.heinerion.invoice.repositories.XmlPersistence;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

@Flogger
@Service
@RequiredArgsConstructor
class LetterRepositoryImpl extends AbstractXmlRepository<Letter> implements LetterRepository {
  private final XmlPersistence persistence;
  private final PathUtilNG pathUtilNG;

  private Collection<Letter> letters = new HashSet<>();

  @PostConstruct
  private void load() {
    this.letters = new HashSet<>(persistence.readLetters(getFilename()));
    log.atInfo().log("%d invoices loaded", letters.size());
  }

  @Override
  public Collection<Letter> findAll() {
    return Collections.unmodifiableCollection(letters);
  }

  @Override
  public Collection<Letter> findAllBySender(Company sender) {
    if (sender == null || sender.getId() == null) {
      return Collections.emptyList();
    }

    return letters.stream()
        .filter(invoice -> invoice.getCompany().getId().equals(sender.getId()))
        .toList();
  }

  @Override
  protected Letter saveInMemory(Letter entry) {
    letters.add(entry);
    return entry;
  }

  @Override
  protected void saveOnDisk() {
    persistence.writeLetters(getFilename(), letters.stream()
        .sorted(Comparator.comparing(Letter::getCompany).thenComparing(Letter::getSubject))
        .toList()
    );
  }

  @Override
  protected Path getFilename() {
    return pathUtilNG.getSystemPath().resolve("letters.xml");
  }
}
