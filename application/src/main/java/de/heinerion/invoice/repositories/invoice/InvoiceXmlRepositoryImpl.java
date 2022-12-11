package de.heinerion.invoice.repositories.invoice;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.*;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Flogger
@Service
@RequiredArgsConstructor
class InvoiceXmlRepositoryImpl extends AbstractXmlRepository<Invoice> implements InvoiceXmlRepository {
  private final XmlPersistence persistence;
  private final InvoiceRepository invoiceRepository;
  private final CompanyRepository companyRepository;
  private final AccountRepository accountRepository;
  private final AddressRepository addressRepository;
  private final ItemRepository itemRepository;
  private final PathUtilNG pathUtilNG;

  private Collection<Invoice> invoices = new HashSet<>();

  @PostConstruct
  private void load() {
    boolean persisted = false;
    Set<Invoice> set = new HashSet<>();
    for (Invoice c : persistence.readInvoices(getFilename())) {
      boolean needsPersisting = isNotPersisted(c);
      persisted |= needsPersisting;
      set.add(needsPersisting ? persist(c) : c);
    }
    this.invoices = set;
    log.atInfo().log("%d invoices loaded", invoices.size());

    if (persisted) {
      log.atInfo().log("needs write to disk");
      saveOnDisk();
    }
  }

  private Invoice persist(Invoice invoice) {
    invoice.setCompany(persist(invoice.getCompany()));
    invoice.setReceiver(addressRepository.save(invoice.getReceiver()));

    Set<Item> items = invoice.getItems().stream()
        .map(itemRepository::save)
        .collect(Collectors.toSet());
    invoice.setItems(items);

    return invoiceRepository.save(invoice);
  }

  private Company persist(Company c) {
    c.setAccount(accountRepository.save(c.getAccount()));
    c.setAddress(addressRepository.save(c.getAddress()));

    return companyRepository.save(c);
  }

  private boolean isNotPersisted(Invoice invoice) {
    log.atInfo().log("check %s@%s", invoice, invoice.getId());
    return invoice.getId() == null || invoiceRepository.findById(invoice.getId()).isEmpty();
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
    Invoice invoice = persist(entry);
    invoices.add(invoice);
    return invoice;
  }

  @Override
  protected void saveOnDisk() {
    persistence.writeInvoices(getFilename(), invoices.stream()
        .sorted(Comparator
            .comparing(Invoice::getCompany)
            .thenComparing(Invoice::getNumber))
        .toList()
    );
  }

  protected Path getFilename() {
    return pathUtilNG.getSystemPath().resolve("invoices.xml");
  }
}
