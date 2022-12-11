package de.heinerion.invoice.repositories;

import de.heinerion.invoice.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
