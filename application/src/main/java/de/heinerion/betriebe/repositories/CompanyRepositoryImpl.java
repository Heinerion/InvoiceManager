package de.heinerion.betriebe.repositories;

import de.heinerion.betriebe.models.Company;
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
public class CompanyRepositoryImpl extends AbstractXmlRepository<Company> implements CompanyRepository {
  private final XmlPersistence persistence;
  private final PathUtilNG pathUtilNG;

  private Collection<Company> companies = new HashSet<>();

  @PostConstruct
  private void load() {
    this.companies = new HashSet<>(persistence.readCompanies(getFilename()));
    log.atInfo().log("%d companies loaded", companies.size());
  }

  @Override
  public Collection<Company> findAll() {
    return Collections.unmodifiableCollection(companies);
  }

  @Override
  protected Company saveInMemory(Company entry) {
    companies.add(entry);
    return entry;
  }

  @Override
  protected void saveOnDisk() {
    persistence.writeCompanies(getFilename(), companies.stream()
        .sorted(Comparator.comparing(Company::getDescriptiveName))
        .toList()
    );
  }

  private File getFilename() {
    return new File(pathUtilNG.getSystemPath(), "companies.xml");
  }
}
