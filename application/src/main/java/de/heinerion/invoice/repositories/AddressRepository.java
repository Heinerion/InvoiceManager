package de.heinerion.invoice.repositories;

import de.heinerion.invoice.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
