package de.heinerion.betriebe.repositories;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.models.Company;

import java.util.Collection;
import java.util.Optional;

public interface TemplateRepository extends Repository<InvoiceTemplate> {
  Collection<InvoiceTemplate> findByCompany(Company company);

  Optional<InvoiceTemplate> findByCompanyAndName(Company company, String name);
}
