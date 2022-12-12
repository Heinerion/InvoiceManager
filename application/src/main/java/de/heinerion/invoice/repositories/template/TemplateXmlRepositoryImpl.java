package de.heinerion.invoice.repositories.template;

import de.heinerion.contract.Contract;
import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.*;
import de.heinerion.invoice.util.PathUtilNG;
import de.heinerion.util.Strings;
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
class TemplateXmlRepositoryImpl extends AbstractXmlRepository<InvoiceTemplate> implements TemplateXmlRepository {
  private final XmlPersistence persistence;
  private final PathUtilNG pathUtilNG;
  private final CompanyRepository companyRepository;
  private final InvoiceTemplateRepository invoiceTemplateRepository;
  private final ItemRepository itemRepository;

  private Collection<InvoiceTemplate> templates = new HashSet<>();

  @PostConstruct
  private void load() {
    Set<InvoiceTemplate> temps = persistence.readTemplates(getFilename())
        .stream()
        .map(this::gatherCompany)
        .map(this::convertInhalt)
        .collect(Collectors.toSet());

    boolean persisted = false;
    Set<InvoiceTemplate> set = new HashSet<>();
    for (InvoiceTemplate c : temps) {
      boolean needsPersisting = isNotPersisted(c);
      persisted |= needsPersisting;
      set.add(needsPersisting ? persist(c) : c);
    }
    this.templates = set;
    log.atInfo().log("%d templates loaded", templates.size());

    if (persisted) {
      log.atInfo().log("needs write to disk");
      saveOnDisk();
    }
  }

  private boolean isNotPersisted(InvoiceTemplate invoice) {
    log.atInfo().log("check %s@%s", invoice, invoice.getId());
    return invoice.getId() == null || invoiceTemplateRepository.findById(invoice.getId()).isEmpty();
  }

  private InvoiceTemplate persist(InvoiceTemplate invoice) {
    Set<Item> items = invoice.getItems().stream()
        .map(itemRepository::save)
        .collect(Collectors.toSet());
    invoice.setItems(items);

    return invoiceTemplateRepository.save(invoice);
  }

  private InvoiceTemplate gatherCompany(InvoiceTemplate t) {
    if (t.getCompanyId() != null) {
      t.setCompany(companyRepository.findById(t.getCompanyId()).orElse(null));
    }
    return t;
  }

  private InvoiceTemplate convertInhalt(InvoiceTemplate t) {
    String[][] inhalt = t.getInhalt();

    Set<Item> items = new HashSet<>();
    for (int i = 0; i < inhalt.length; i++) {
      items.add(parseItem(i, inhalt[i]));
    }
    t.setItems(items);

    return t;
  }

  private Item parseItem(int position, String[] token) {
    final int NAME = 0;
    final int UNIT = 1;
    final int PPU = 2;
    final int COUNT = 3;

    return anyIsNullOrEmpty(token[UNIT], token[PPU], token[COUNT])
        ? Item.of(position, token[NAME])
        : Item.of(position, token[NAME], token[UNIT], toDouble(token[PPU]), toDouble(token[COUNT]));
  }

  private boolean anyIsNullOrEmpty(String... s) {
    return Arrays.asList(s).stream().anyMatch(Strings::isBlank);
  }

  private double toDouble(String source) {
    Contract.requireNotNull(source, "source");
    return Double.parseDouble(source);
  }

  @Override
  protected Path getFilename() {
    return pathUtilNG.getSystemPath().resolve("templates.xml");
  }

  @Override
  public Collection<InvoiceTemplate> findByCompany(Company company) {
    return templates.stream()
        .filter(template -> Objects.equals(template.getCompany(), company))
        .toList();
  }

  @Override
  public Optional<InvoiceTemplate> findByCompanyAndName(Company company, String name) {
    return templates.stream()
        .filter(t -> t.getName().equals(name))
        .findFirst();
  }

  @Override
  protected InvoiceTemplate saveInMemory(InvoiceTemplate entry) {
    templates.add(entry);
    return entry;
  }

  @Override
  protected void saveOnDisk() {
    persistence.writeTemplates(getFilename(), templates.stream()
        .sorted(Comparator.comparing(InvoiceTemplate::getName))
        .toList());
  }

  @Override
  public Collection<InvoiceTemplate> findAll() {
    return Collections.unmodifiableCollection(templates);
  }
}
