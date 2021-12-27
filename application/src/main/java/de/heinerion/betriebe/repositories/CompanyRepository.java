package de.heinerion.betriebe.repositories;

import de.heinerion.betriebe.models.Company;

import java.util.Optional;

public interface CompanyRepository extends Repository<Company> {
  Optional<Company> findByOfficialName(String companyName);
}
