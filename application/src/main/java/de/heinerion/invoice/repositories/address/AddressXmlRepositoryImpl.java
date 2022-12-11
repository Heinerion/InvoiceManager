package de.heinerion.invoice.repositories.address;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.*;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.*;

@Service
@Flogger
@RequiredArgsConstructor
class AddressXmlRepositoryImpl extends AbstractXmlRepository<Address> implements AddressXmlRepository {
  private final XmlPersistence persistence;
  private final PathUtilNG pathUtilNG;
  private final AddressRepository addressRepository;

  private Collection<Address> addresses = new HashSet<>();

  @PostConstruct
  private void load() {
    boolean persisted = false;
    Set<Address> set = new HashSet<>();
    for (Address a : persistence.readAddresses(getFilename())) {
      boolean needsPersisting = isNotPersisted(a);
      persisted |= needsPersisting;
      set.add(needsPersisting ? addressRepository.save(a) : a);
    }
    this.addresses = set;
    log.atInfo().log("%d addresses loaded", this.addresses.size());

    if (persisted) {
      log.atInfo().log("needs write to disk");
      saveOnDisk();
    }
  }

  private boolean isNotPersisted(Address a) {
    log.atInfo().log("check %s@%s", a, a.getId());
    return a.getId() == null || addressRepository.findById(a.getId()).isEmpty();
  }

  @Override
  protected Path getFilename() {
    return pathUtilNG.getSystemPath().resolve("addresses.xml");
  }

  @Override
  public Collection<Address> findByCompany(Company company) {
    return addresses.stream()
        .sorted(Comparator.comparing(Address::getRecipient))
        .toList();
  }

  @Override
  public Optional<Address> findByRecipient(String recipient) {
    return addresses.stream()
        .filter(a -> a.getRecipient().equals(recipient))
        .findFirst();
  }

  @Override
  public Optional<Address> findByCompanyAndRecipient(Company company, String recipient) {
    // TODO Company-Mapping is broken, re-implementation needed!
    return findByRecipient(recipient);
  }

  @Override
  public Collection<Address> findAll() {
    return Collections.unmodifiableCollection(addresses);
  }

  protected Address saveInMemory(Address address) {
    Address persisted = addressRepository.save(address);
    addresses.add(persisted);
    return persisted;
  }

  protected void saveOnDisk() {
    persistence.writeAddresses(getFilename(), addresses.stream()
        .sorted(Comparator.comparing(Address::getRecipient))
        .toList());
  }
}
