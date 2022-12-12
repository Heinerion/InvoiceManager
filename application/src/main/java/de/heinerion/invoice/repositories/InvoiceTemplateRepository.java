package de.heinerion.invoice.repositories;

import de.heinerion.invoice.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface InvoiceTemplateRepository extends JpaRepository<InvoiceTemplate, Long> {

  Collection<InvoiceTemplate> findByCompany(Company company);
}
