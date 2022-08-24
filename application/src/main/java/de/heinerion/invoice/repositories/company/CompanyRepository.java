package de.heinerion.invoice.repositories.company;

import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.repositories.Repository;

import java.util.Optional;

public interface CompanyRepository extends Repository<Company> {
  Optional<Company> findByOfficialName(String companyName);
}
