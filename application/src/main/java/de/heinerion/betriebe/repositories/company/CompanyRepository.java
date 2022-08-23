package de.heinerion.betriebe.repositories.company;

import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.repositories.Repository;

import java.util.Optional;

public interface CompanyRepository extends Repository<Company> {
  Optional<Company> findByOfficialName(String companyName);
}
