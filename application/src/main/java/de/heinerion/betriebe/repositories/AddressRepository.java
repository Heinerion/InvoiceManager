package de.heinerion.betriebe.repositories;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;

import java.util.Collection;
import java.util.Optional;

public interface AddressRepository extends Repository<Address> {
  Collection<Address> findByCompany(Company company);

  Optional<Address> findByCompanyAndRecipient(Company company, String recipient);

  Optional<Address> findByRecipient(String recipient);
}
