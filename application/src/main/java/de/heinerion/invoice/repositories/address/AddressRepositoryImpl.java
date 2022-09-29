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
class AddressRepositoryImpl extends AbstractXmlRepository<Address> implements AddressRepository {
  private final XmlPersistence persistence;
  private final PathUtilNG pathUtilNG;

  private Collection<Address> addresses = new HashSet<>();

  @PostConstruct
  private void load() {
    this.addresses = new HashSet<>(persistence.readAddresses(getFilename()));
    log.atInfo().log("%d addresses loaded", addresses.size());
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
    findByRecipient(address.getRecipient())
        .ifPresent(addresses::remove);
    addresses.add(address);
    return address;
  }

  protected void saveOnDisk() {
    persistence.writeAddresses(getFilename(), addresses.stream()
        .sorted(Comparator.comparing(Address::getRecipient))
        .toList());
  }
}
