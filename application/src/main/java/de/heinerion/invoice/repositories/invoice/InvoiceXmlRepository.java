package de.heinerion.invoice.repositories.invoice;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.XmlRepository;

import java.util.Collection;

public interface InvoiceXmlRepository extends XmlRepository<Invoice> {
  Collection<Invoice> findAllBySender(Company sender);
}
