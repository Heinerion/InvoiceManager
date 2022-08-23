package de.heinerion.betriebe.repositories.address;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.repositories.Repository;

import java.util.Collection;
import java.util.Optional;

public interface AddressRepository extends Repository<Address> {
  Collection<Address> findByCompany(Company company);

  Optional<Address> findByCompanyAndRecipient(Company company, String recipient);

  Optional<Address> findByRecipient(String recipient);
}
