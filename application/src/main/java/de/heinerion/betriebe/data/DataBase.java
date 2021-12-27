package de.heinerion.betriebe.data;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.repositories.AddressRepository;
import de.heinerion.betriebe.repositories.CompanyRepository;
import de.heinerion.invoice.storage.loading.LoadListener;
import de.heinerion.invoice.storage.loading.Loadable;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoiceTable;
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

  private ArchivedInvoiceTable invoices;

  private String lastMessage;

  public ArchivedInvoiceTable getInvoices() {
    if (invoices == null) {
      setInvoices(new ArchivedInvoiceTable());
    }

    return invoices;
  }

  private void setInvoices(ArchivedInvoiceTable archivedInvoices) {
    invoices = archivedInvoices;
  }

  void addInvoice(ArchivedInvoice archivedInvoice) {
    if (isEntryNotInList(archivedInvoice, getInvoices())) {
      getInvoices().add(archivedInvoice);
    }
  }

  private boolean isEntryNotInList(ArchivedInvoice archivedInvoice, ArchivedInvoiceTable list) {
    return list != null && !list.contains(archivedInvoice);
  }

  void addLoadable(Loadable loadable) {
    if (loadable instanceof Address address) {
      addressRepository.save(address);
    } else if (loadable instanceof ArchivedInvoice invoice) {
      addInvoice(invoice);
    } else if (loadable instanceof Company company) {
      Session.addAvailableCompany(company);
      companyRepository.save(company);
    }
  }

  @Override
  public void notifyLoading(String message, Loadable loadable) {
    if (!message.equals(lastMessage)) {
      lastMessage = message;
    }

    addLoadable(loadable);
  }
}
