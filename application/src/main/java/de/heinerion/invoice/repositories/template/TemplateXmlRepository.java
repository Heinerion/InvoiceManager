package de.heinerion.invoice.repositories.template;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.XmlRepository;

import java.util.*;

public interface TemplateXmlRepository extends XmlRepository<InvoiceTemplate> {
  Collection<InvoiceTemplate> findByCompany(Company company);

  Optional<InvoiceTemplate> findByCompanyAndName(Company company, String name);
}
