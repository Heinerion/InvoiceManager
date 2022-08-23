package de.heinerion.betriebe.repositories.template;

import de.heinerion.betriebe.models.InvoiceTemplate;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.repositories.Repository;

import java.util.Collection;
import java.util.Optional;

public interface TemplateRepository extends Repository<InvoiceTemplate> {
  Collection<InvoiceTemplate> findByCompany(Company company);

  Optional<InvoiceTemplate> findByCompanyAndName(Company company, String name);
}
