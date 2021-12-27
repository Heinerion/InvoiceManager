package de.heinerion.betriebe.repositories;

import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;

import java.util.Collection;

public interface InvoiceRepository extends Repository<Invoice> {
  Collection<Invoice> findAllBySender(Company sender);
}
