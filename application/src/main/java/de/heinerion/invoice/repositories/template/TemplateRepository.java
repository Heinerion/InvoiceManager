package de.heinerion.invoice.repositories.template;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.Repository;

import java.util.*;

public interface TemplateRepository extends Repository<InvoiceTemplate> {
  Collection<InvoiceTemplate> findByCompany(Company company);

  Optional<InvoiceTemplate> findByCompanyAndName(Company company, String name);
}
