package de.heinerion.invoice.repositories.template;

import de.heinerion.invoice.models.InvoiceTemplate;
import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.repositories.Repository;

import java.util.Collection;
import java.util.Optional;

public interface TemplateRepository extends Repository<InvoiceTemplate> {
  Collection<InvoiceTemplate> findByCompany(Company company);

  Optional<InvoiceTemplate> findByCompanyAndName(Company company, String name);
}
