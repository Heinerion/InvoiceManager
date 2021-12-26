package de.heinerion.betriebe.repositories;

import de.heinerion.betriebe.data.MemoryBank;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.storage.loading.TextFileLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
@Flogger
@RequiredArgsConstructor
class AddressRepositoryImpl implements AddressRepository {
  private final MemoryBank memory;
  private final TextFileLoader fileLoader;

  @Override
  public Collection<Address> findByCompany(Company company) {
    return memory.getAddresses(company);
  }

  @Override
  public Optional<Address> findByCompanyAndRecipient(Company company, String recipient) {
    return memory.getAddress(company, recipient);
  }

  @Override
  public Collection<Address> findAll() {
    return memory.getAllAddresses();
  }

  @Override
  public Address save(Address address) {
    memory.addAddress(address);
    fileLoader.saveAddresses(findAll());
    return address;
  }

  @Override
  public Collection<Address> saveAll(Collection<Address> entries) {
    Collection<Address> savedEntries = new HashSet<>();
    for (Address address : entries) {
      try {
        memory.addAddress(address);
        savedEntries.add(address);
      } catch (Exception e) {
        log.atWarning()
            .withCause(e)
            .log("Adress could not be saved: %s", address);
      }
    }

    fileLoader.saveAddresses(findAll());
    return savedEntries;
  }
}
