package de.heinerion.invoice.repositories.invoice;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.Repository;

import java.util.Collection;

public interface InvoiceRepository extends Repository<Invoice> {
  Collection<Invoice> findAllBySender(Company sender);
}
