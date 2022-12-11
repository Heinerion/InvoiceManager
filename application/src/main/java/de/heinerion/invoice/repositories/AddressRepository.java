package de.heinerion.invoice.repositories;

import de.heinerion.invoice.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface AddressRepository extends JpaRepository<Address, Long> {
  Collection<Address> findByOwner(Company company);
}
