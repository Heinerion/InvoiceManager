package de.heinerion.invoice.repositories.address;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.Repository;

import java.util.*;

public interface AddressRepository extends Repository<Address> {
  Collection<Address> findByCompany(Company company);

  Optional<Address> findByCompanyAndRecipient(Company company, String recipient);

  Optional<Address> findByRecipient(String recipient);
}
