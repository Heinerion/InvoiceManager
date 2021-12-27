package de.heinerion.betriebe.data;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.repositories.AddressRepository;
import de.heinerion.betriebe.repositories.CompanyRepository;
import de.heinerion.invoice.storage.loading.LoadListener;
import de.heinerion.invoice.storage.loading.Loadable;
import de.heinerion.invoice.storage.loading.XmlLoader;
import de.heinerion.invoice.view.common.StatusComponent;
import de.heinerion.invoice.view.swing.FormatUtil;
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
  private final XmlLoader xmlLoader;
  private final AddressRepository addressRepository;
  private final CompanyRepository companyRepository;

  private ArchivedInvoiceTable invoices;

  private StatusComponent progress;

  private String lastMessage;

  /**
   * Removes and loads every business class from the file system via {@link XmlLoader}
   * <p>
   * Calls {@link #load(StatusComponent)} with the last used {@link StatusComponent} or {@code null}, if none was used
   * before.
   * </p>
   */
  public void load() {
    load(progress);
  }

  /**
   * Removes and loads every business class from the file system via {@link XmlLoader}
   *
   * @param indicator will be used for {@link XmlLoader#load(StatusComponent, LoadListener, DataBase, AddressRepository)}  and for later calls via {@link
   *                  #load()}
   */
  public void load(StatusComponent indicator) {
    progress = indicator;

    removeAllInvoices();

    xmlLoader.load(progress, this, this, addressRepository);
    getInvoices().determineHighestInvoiceNumbers();
  }


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

  public void removeAllInvoices() {
    setInvoices(new ArchivedInvoiceTable());
  }

  @Override
  public void notifyLoading(String message, Loadable loadable) {
    if (!message.equals(lastMessage)) {
      lastMessage = message;
    }

    if (progress != null) {
      progress.incrementProgress();

      double percentage = progress.getProgressPercentage();
      progress.setMessage("%s ({%s})"
          .formatted(message, FormatUtil.formatPercentage(percentage)));
    }

    addLoadable(loadable);
  }
}
