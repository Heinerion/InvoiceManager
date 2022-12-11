package de.heinerion.invoice.repositories.template;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.*;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.*;

@Flogger
@Service
@RequiredArgsConstructor
class TemplateXmlRepositoryImpl extends AbstractXmlRepository<InvoiceTemplate> implements TemplateXmlRepository {
  private final XmlPersistence persistence;
  private final PathUtilNG pathUtilNG;

  private Collection<InvoiceTemplate> templates = new HashSet<>();

  @PostConstruct
  private void load() {
    this.templates = new HashSet<>(persistence.readTemplates(getFilename()));
    log.atInfo().log("%d templates loaded", templates.size());
  }

  @Override
  protected Path getFilename() {
    return pathUtilNG.getSystemPath().resolve("templates.xml");
  }

  @Override
  public Collection<InvoiceTemplate> findByCompany(Company company) {
    return templates.stream()
        .filter(template -> Objects.equals(template.getCompanyId(), company.getId()))
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
