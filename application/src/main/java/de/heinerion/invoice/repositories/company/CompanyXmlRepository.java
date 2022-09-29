package de.heinerion.invoice.repositories.company;

import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.repositories.XmlRepository;

import java.util.Optional;

public interface CompanyXmlRepository extends XmlRepository<Company> {
  Optional<Company> findByOfficialName(String companyName);
}
