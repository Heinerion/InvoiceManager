package de.heinerion.invoice.repositories.address;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.XmlRepository;

import java.util.*;

public interface AddressXmlRepository extends XmlRepository<Address> {
  Collection<Address> findByCompany(Company company);

  Optional<Address> findByCompanyAndRecipient(Company company, String recipient);

  Optional<Address> findByRecipient(String recipient);
}
