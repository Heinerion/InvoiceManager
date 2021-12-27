package de.heinerion.betriebe.repositories;

import de.heinerion.betriebe.models.InvoiceTemplate;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.storage.xml.jaxb.XmlPersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Flogger
@Service
@RequiredArgsConstructor
class TemplateRepositoryImpl extends AbstractXmlRepository<InvoiceTemplate> implements TemplateRepository {
  private final XmlPersistence persistence;
  private final PathUtilNG pathUtilNG;

  private Collection<InvoiceTemplate> templates = new HashSet<>();

  @PostConstruct
  private void load() {
    this.templates = new HashSet<>(persistence.readTemplates(getFilename()));
    log.atInfo().log("%d templates loaded", templates.size());
  }

  private File getFilename() {
    return new File(pathUtilNG.getSystemPath(), "templates.xml");
  }

  @Override
  public Collection<InvoiceTemplate> findByCompany(Company company) {
    return templates.stream()
        .filter(template -> template.getCompanyId().equals(company.getId()))
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
