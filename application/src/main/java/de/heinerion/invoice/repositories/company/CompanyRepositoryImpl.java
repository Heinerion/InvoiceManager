package de.heinerion.invoice.repositories.company;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.Company;
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
class CompanyRepositoryImpl extends AbstractXmlRepository<Company> implements CompanyRepository {
  private final XmlPersistence persistence;
  private final PathUtilNG pathUtilNG;
  private final Session session = Session.getInstance();

  private Collection<Company> companies = new HashSet<>();

  @PostConstruct
  private void load() {
    this.companies = new HashSet<>(persistence.readCompanies(getFilename()));
    log.atInfo().log("%d companies loaded", companies.size());
    companies.stream()
        .sorted(Comparator.comparing(Company::getDescriptiveName))
        .forEach(session::addAvailableCompany);
  }

  @Override
  public Collection<Company> findAll() {
    return Collections.unmodifiableCollection(companies);
  }

  @Override
  public Optional<Company> findByOfficialName(String companyName) {
    if (companyName == null) {
      return Optional.empty();
    }

    return companies.stream()
        .filter(company -> company.getOfficialName().equals(companyName))
        .findFirst();
  }

  @Override
  protected Company saveInMemory(Company entry) {
    companies.add(entry);
    if (entry.getId() == null) {
      entry.setId(UUID.randomUUID());
    }
    return entry;
  }

  @Override
  protected void saveOnDisk() {
    persistence.writeCompanies(getFilename(), companies.stream()
        .sorted(Comparator.comparing(Company::getDescriptiveName))
        .toList()
    );
  }

  @Override
  protected Path getFilename() {
    return pathUtilNG.getSystemPath().resolve("companies.xml");
  }
}
