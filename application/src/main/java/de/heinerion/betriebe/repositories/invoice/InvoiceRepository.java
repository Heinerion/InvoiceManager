package de.heinerion.betriebe.repositories.invoice;

import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.repositories.Repository;

import java.util.Collection;

public interface InvoiceRepository extends Repository<Invoice> {
  Collection<Invoice> findAllBySender(Company sender);
}
