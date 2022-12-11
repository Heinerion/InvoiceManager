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
class CompanyXmlRepositoryImpl extends AbstractXmlRepository<Company> implements CompanyXmlRepository {
  private final XmlPersistence persistence;
  private final CompanyRepository companyRepository;
  private final AccountRepository accountRepository;
  private final AddressRepository addressRepository;
  private final Session session = Session.getInstance();
  private final PathUtilNG pathUtilNG;

  private Collection<Company> companies = new HashSet<>();

  @PostConstruct
  private void load() {
    boolean persisted = false;
    Set<Company> set = new HashSet<>();
    for (Company c : persistence.readCompanies(getFilename())) {
      boolean needsPersisting = isNotPersisted(c);
      persisted |= needsPersisting;
      set.add(needsPersisting ? persist(c) : c);
    }
    this.companies = set;
    log.atInfo().log("%d companies loaded", this.companies.size());

    this.companies.stream()
        .sorted(Comparator.comparing(Company::getDescriptiveName))
        .forEach(session::addAvailableCompany);

    if (persisted) {
      log.atInfo().log("needs write to disk");
      saveOnDisk();
    }
  }

  private Company persist(Company c) {
    c.setAccount(accountRepository.save(c.getAccount()));
    c.setAddress(addressRepository.save(c.getAddress()));

    return companyRepository.save(c);
  }

  private boolean isNotPersisted(Company a) {
    log.atInfo().log("check %s@%s", a, a.getId());
    return a.getId() == null || companyRepository.findById(a.getId()).isEmpty();
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
    Company company = persist(entry);
    companies.add(company);
    return company;
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
