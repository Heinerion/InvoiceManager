package de.heinerion.invoice.repositories.invoice;

import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.models.Invoice;
import de.heinerion.invoice.repositories.AbstractXmlRepository;
import de.heinerion.invoice.repositories.XmlPersistence;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

@Flogger
@Service
@RequiredArgsConstructor
class InvoiceRepositoryImpl extends AbstractXmlRepository<Invoice> implements InvoiceRepository {
  private final XmlPersistence persistence;
  private final PathUtilNG pathUtilNG;

  private Collection<Invoice> invoices = new HashSet<>();

  @PostConstruct
  private void load() {
    this.invoices = new HashSet<>(persistence.readInvoices(getFilename()));
    log.atInfo().log("%d invoices loaded", invoices.size());
  }

  @Override
  public Collection<Invoice> findAll() {
    return Collections.unmodifiableCollection(invoices);
  }

  @Override
  public Collection<Invoice> findAllBySender(Company sender) {
    if (sender == null || sender.getId() == null) {
      return Collections.emptyList();
    }

    return invoices.stream()
        .filter(invoice -> invoice.getCompany().getId().equals(sender.getId()))
        .toList();
  }

  @Override
  protected Invoice saveInMemory(Invoice entry) {
    invoices.add(entry);
    return entry;
  }

  @Override
  protected void saveOnDisk() {
    persistence.writeInvoices(getFilename(), invoices.stream()
        .sorted(Comparator.comparing(Invoice::getCompany).thenComparing(Invoice::getNumber))
        .toList()
    );
  }

  protected Path getFilename() {
    return pathUtilNG.getSystemPath().resolve("invoices.xml");
  }
}
