package de.heinerion.betriebe.repositories;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.storage.xml.jaxb.XmlPersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Service
@Flogger
@RequiredArgsConstructor
class AddressRepositoryImpl implements AddressRepository {
  private final XmlPersistence persistence;
  private final PathUtilNG pathUtilNG;

  private Collection<Address> addresses = new HashSet<>();

  @PostConstruct
  private void load() {
    this.addresses = new HashSet<>(persistence.readAddresses(getFilename()));
    log.atInfo().log("%d addresses loaded", addresses.size());
  }

  private File getFilename() {
    return new File(pathUtilNG.getSystemPath(), "addresses.xml");
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

  @Override
  public Address save(Address address) {
    saveInMemory(address);
    saveOnDisk();
    return address;
  }

  private Address saveInMemory(Address address) {
    findByRecipient(address.getRecipient())
        .ifPresent(addresses::remove);
    addresses.add(address);
    return address;
  }

  private void saveOnDisk() {
    persistence.writeAddresses(getFilename(), addresses.stream()
        .sorted(Comparator.comparing(Address::getRecipient))
        .toList());
  }

  @Override
  public Collection<Address> saveAll(Collection<Address> entries) {
    Collection<Address> savedEntries = new HashSet<>();
    for (Address address : entries) {
      try {
        savedEntries.add(saveInMemory(address));
      } catch (Exception e) {
        log.atWarning()
            .withCause(e)
            .log("Adress could not be saved: %s", address);
      }
    }

    saveOnDisk();
    return savedEntries;
  }
}
