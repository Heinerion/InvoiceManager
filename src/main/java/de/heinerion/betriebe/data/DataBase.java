package de.heinerion.betriebe.data;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.data.listable.TexTemplate;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.storage.loading.IO;
import de.heinerion.invoice.storage.loading.LoadListener;
import de.heinerion.invoice.storage.loading.Loadable;
import de.heinerion.invoice.view.common.StatusComponent;
import de.heinerion.invoice.view.swing.FormatUtil;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoiceTable;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The DataBase is used for all storing and loading of any business class.
 * <p>
 * Only one DataBase exists at a time, which can be retrieved by {@link #getInstance()}
 * </p>
 * <p>
 * Entities are held in this class' only instance, thus in memory
 * </p>
 */
public final class DataBase implements LoadListener {
  private final MemoryBank memory;

  private ArchivedInvoiceTable invoices;

  private IO io;
  private StatusComponent<?> progress;

  private String lastMessage;

  private static final DataBase instance = new DataBase();

  private DataBase() {
    this.memory = new MemoryBank();
  }

  /**
   * @return the only instance of this class, used for all business class storage and loading
   */
  public static DataBase getInstance() {
    return instance;
  }

  /**
   * Collects all {@link Address}es valid for the given {@link Company}
   *
   * @param company to be filtered by
   *
   * @return every {@link Address} connected to this {@link Company} sorted by {@link Address#getRecipient()}
   *
   * @see MemoryBank
   */
  public List<Address> getAddresses(Company company) {
    return memory.getAddresses(company);
  }

  /**
   * Sets the {@link IO} instance
   *
   * @param io the instance to be used for loading and saving
   */
  public void setIo(IO io) {
    this.io = Objects.requireNonNull(io);
  }

  /**
   * Removes and loads every business class from the file system via {@link IO}
   * <p>
   * Calls {@link #load(StatusComponent)} with the last used {@link StatusComponent} or {@code null}, if none was used
   * before.
   * </p>
   */
  public void load() {
    load(progress);
  }

  /**
   * Removes and loads every business class from the file system via {@link IO}
   *
   * @param indicator will be used for {@link IO#load(StatusComponent, LoadListener)} and for later calls via {@link
   *                  #load()}
   */
  public void load(StatusComponent<?> indicator) {
    progress = indicator;

    removeAllInvoices();
    memory.reset();

    io.load(progress, this);
    getInvoices().determineHighestInvoiceNumbers();

    io.loadInvoiceTemplates().forEach(this::addTemplates);
    io.loadTexTemplates().forEach(this::addTexTemplate);
  }

  /**
   * Looks for the given recipients {@link Address} in the addresses valid for the given {@link Company}.
   *
   * @param company   to be filtered by
   * @param recipient to be looked for
   *
   * @return an {@link Address} with the given recipient
   *
   * @see MemoryBank
   */
  public Optional<Address> getAddress(Company company, String recipient) {
    return memory.getAddress(company, recipient);
  }

  /**
   * @return all available Addresses
   */
  public List<Address> getAddresses() {
    return memory.getAllAddresses();
  }

  public void addAddress(Address address) {
    memory.addAddress(address);

    io.saveAddresses(getAddresses());
  }

  List<Company> getCompanies() {
    return memory.getAllCompanies();
  }

  void addCompany(Company company) {
    Session.addAvailableCompany(company);

    memory.addCompany(company);
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

 public List<InvoiceTemplate> getTemplates(Company company) {
    return Collections.unmodifiableList(memory.getTemplates(company));
  }

  public void addTemplate(Company company, InvoiceTemplate template) {
    memory.addTemplate(company, template);
  }

  private void addTemplates(Company company, List<InvoiceTemplate> invoiceTemplates) {
    invoiceTemplates.forEach(template -> addTemplate(company, template));
  }

  List<TexTemplate> getTexTemplates(Company company) {
    return memory.getTexTemplates(company);
  }

  private void addTexTemplate(TexTemplate template) {
    addTexTemplate(null, template);
  }

  void addTexTemplate(Company company, TexTemplate template) {
    memory.addTexTemplate(company, template);
  }

  void clearAllLists() {
    memory.reset();
  }

  private boolean isEntryNotInList(ArchivedInvoice archivedInvoice, ArchivedInvoiceTable list) {
    return list != null && !list.contains(archivedInvoice);
  }

  void addLoadable(Loadable loadable) {
    if (loadable instanceof Address) {
      addAddress((Address) loadable);
    } else if (loadable instanceof ArchivedInvoice) {
      addInvoice((ArchivedInvoice) loadable);
    } else if (loadable instanceof Company) {
      addCompany((Company) loadable);
    }
  }

  private void removeAllInvoices() {
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
      String status = MessageFormat.format("{0} ({1})", message, FormatUtil.formatPercentage(percentage));
      progress.setMessage(status);
    }

    addLoadable(loadable);
  }
}
