package de.heinerion.invoice.repositories.letter;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.*;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.*;

@Flogger
@Service
@RequiredArgsConstructor
class LetterXmlRepositoryImpl extends AbstractXmlRepository<Letter> implements LetterXmlRepository {
  private final XmlPersistence persistence;
  private final LetterRepository letterRepository;
  private final CompanyRepository companyRepository;
  private final AccountRepository accountRepository;
  private final AddressRepository addressRepository;
  private final PathUtilNG pathUtilNG;

  private Collection<Letter> letters = new HashSet<>();

  @PostConstruct
  private void load() {
    boolean persisted = false;
    Set<Letter> set = new HashSet<>();
    for (Letter letter : persistence.readLetters(getFilename())) {
      boolean needsPersisting = isNotPersisted(letter);
      persisted |= needsPersisting;
      set.add(needsPersisting ? persist(letter) : letter);
    }
    this.letters = set;
    log.atInfo().log("%d letters loaded", letters.size());

    if (persisted) {
      log.atInfo().log("needs write to disk");
      saveOnDisk();
    }
  }

  private Letter persist(Letter invoice) {
    invoice.setCompany(persist(invoice.getCompany()));
    invoice.setReceiver(addressRepository.save(invoice.getReceiver()));

    return letterRepository.save(invoice);
  }

  private Company persist(Company c) {
    c.setAccount(accountRepository.save(c.getAccount()));
    c.setAddress(addressRepository.save(c.getAddress()));

    return companyRepository.save(c);
  }

  private boolean isNotPersisted(Letter invoice) {
    log.atInfo().log("check %s@%s", invoice, invoice.getId());
    return invoice.getId() == null || letterRepository.findById(invoice.getId()).isEmpty();
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
    Letter letter = letterRepository.save(entry);
    letters.add(letter);
    return letter;
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
