package de.heinerion.betriebe.repositories;

import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.storage.xml.jaxb.XmlPersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
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

  private File getFilename() {
    return new File(pathUtilNG.getSystemPath(), "invoices.xml");
  }
}
