package de.heinerion.betriebe.data;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.repositories.AddressRepository;
import de.heinerion.betriebe.repositories.CompanyRepository;
import de.heinerion.invoice.storage.loading.LoadListener;
import de.heinerion.invoice.storage.loading.Loadable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The DataBase is used for all storing and loading of any business class.
 * <p>
 * Entities are held in this class' only instance, thus in memory
 * </p>
 */
@Service
@RequiredArgsConstructor
public class DataBase implements LoadListener {
  private final AddressRepository addressRepository;
  private final CompanyRepository companyRepository;

  void addLoadable(Loadable loadable) {
    if (loadable instanceof Address address) {
      addressRepository.save(address);
    } else if (loadable instanceof Company company) {
      Session.addAvailableCompany(company);
      companyRepository.save(company);
    }
  }

  @Override
  public void notifyLoading(String message, Loadable loadable) {
    addLoadable(loadable);
  }
}
