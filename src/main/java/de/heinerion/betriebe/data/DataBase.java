package de.heinerion.betriebe.data;

import de.heinerion.betriebe.data.listable.DropListable;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

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
  private static final Logger logger = LogManager.getLogger(DataBase.class);

  private List<ListEntry<Address>> addressEntries;
  private List<ListEntry<Company>> companyEntries;
  private List<ListEntry<InvoiceTemplate>> templateEntries;
  private List<ListEntry<TexTemplate>> texTemplateEntries;

  private ArchivedInvoiceTable invoices;

  private IO io;
  private StatusComponent<?> progress;

  private String lastMessage;

  private static DataBase instance = new DataBase();

  private DataBase() {
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
   * @return every {@link Address} connected to this {@link Company} sorted by {@link Address#getRecipient()}
   */
  public List<Address> getAddresses(Company company) {
    List<Address> ret = getEntries(getAddressEntries(), company);
    ret.sort(Comparator.comparing(Address::getRecipient));
    return ret;
  }

  /**
   * Sets the {@link IO} instance to be used for {@link #load(StatusComponent)}, {@link #load()},
   * {@link #addAddress(Address)} and {@link #updateTemplates(List)}.
   *
   * @param io the instance to be used for loading and saving
   */
  public void setIo(IO io) {
    this.io = Objects.requireNonNull(io);
  }

  /**
   * Removes and loads every business class from the file system via {@link IO}
   * <p>
   * Calls {@link #load(StatusComponent)} with the last used {@link StatusComponent} or {@code null}, if none was used before.
   * </p>
   */
  public void load() {
    load(progress);
  }

  /**
   * Removes and loads every business class from the file system via {@link IO}
   *
   * @param indicator will be used for {@link IO#load(StatusComponent, LoadListener)} and for later calls via {@link #load()}
   */
  public void load(StatusComponent<?> indicator) {
    progress = indicator;

    removeAllInvoices();

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
   * @return an {@link Address} with the given recipient
   */
  public Optional<Address> getAddress(Company company, String recipient) {
    Optional<Address> result = Optional.empty();

    if (logger.isDebugEnabled()) {
      logger.debug("getAddress({}, {})", company, recipient);
    }

    ListEntry<Address> entry = getAddressEntry(company, recipient);
    if (entry != null) {
      result = Optional.of(entry.getEntry());
    }

    return result;
  }

  private ListEntry<Address> getAddressEntry(Company company, String recipient) {
    ListEntry<Address> result = null;

    for (ListEntry<Address> address : getAddressEntries()) {
      if (hasFittingCompany(address, company) && hasThisRecipient(address, recipient)) {
        result = address;
        break;
      }
    }

    return result;
  }

  /**
   * @return all available Addresses
   */
  public List<Address> getAddresses() {
    return getAddresses(null);
  }

  private List<ListEntry<Address>> getAddressEntries() {
    if (addressEntries == null) {
      setAddressEntries(new ArrayList<>());
    }

    return addressEntries;
  }

  private void setAddressEntries(ArrayList<ListEntry<Address>> addresses) {
    this.addressEntries = addresses;
  }

  void addAddress(Company company, Address address) {
    ListEntry<Address> oldAddress = getAddressEntry(company, address.getRecipient());

    if (oldAddress == null) {
      getAddressEntries().add(new ListEntry<>(company, address));
    } else {
      oldAddress.setEntry(address);
    }
  }

  public void addAddress(Address address) {
    addAddress(null, address);

    io.saveAddresses(getAddresses());
  }

  Optional<Company> getCompany(String descriptiveName) {
    Optional<Company> result = Optional.empty();

    if (logger.isDebugEnabled()) {
      logger.debug("getCompany({})", descriptiveName);
    }

    ListEntry<Company> entry = getCompanyEntry(descriptiveName);
    if (entry != null) {
      result = Optional.ofNullable(entry.getEntry());
    }

    return result;
  }

  private ListEntry<Company> getCompanyEntry(String descriptiveName) {
    ListEntry<Company> result = null;

    for (ListEntry<Company> company : getCompanyEntries()) {
      if (hasThisDescriptiveName(company, descriptiveName)) {
        result = company;
        break;
      }
    }

    return result;
  }

  List<Company> getCompanies() {
    List<Company> result = getEntries(getCompanyEntries(), null);
    result.sort(Comparator.comparing(Company::getDescriptiveName));
    return result;
  }

  private List<ListEntry<Company>> getCompanyEntries() {
    if (companyEntries == null) {
      setCompanyEntries(new ArrayList<>());
    }

    return companyEntries;
  }

  private void setCompanyEntries(List<ListEntry<Company>> companies) {
    companyEntries = companies;
  }

  void addCompany(Company company) {
    Session.addAvailableCompany(company);

    ListEntry<Company> oldCompany = getCompanyEntry(company.getDescriptiveName());

    if (oldCompany == null) {
      getCompanyEntries().add(new ListEntry<>(company));
    } else {
      oldCompany.setEntry(company);
    }
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

  private ListEntry<InvoiceTemplate> getTemplateEntry(Company company, String templateName) {
    return getTemplateEntry(company, templateName, getTemplateEntries());
  }

  public List<InvoiceTemplate> getTemplates(Company company) {
    return getEntries(getTemplateEntries(), company);
  }

  private List<ListEntry<InvoiceTemplate>> getTemplateEntries() {
    if (templateEntries == null) {
      setTemplateEntries(new ArrayList<>());
    }

    return templateEntries;
  }

  private void setTemplateEntries(List<ListEntry<InvoiceTemplate>> templates) {
    templateEntries = templates;
  }

  void addTemplate(Company company, InvoiceTemplate template) {
    ListEntry<InvoiceTemplate> oldAddress = getTemplateEntry(company, template.getName());

    if (oldAddress == null) {
      getTemplateEntries().add(new ListEntry<>(company, template));
    } else {
      oldAddress.setEntry(template);
    }
  }

  public void updateTemplates(List<InvoiceTemplate> templates) {
    io.saveInvoiceTemplates(templates, Session.getActiveCompany());
    io.loadInvoiceTemplates().forEach(this::addTemplates);
  }

  private void addTemplates(Company company, List<InvoiceTemplate> invoiceTemplates) {
    invoiceTemplates.forEach(template -> addTemplate(company, template));
  }

  private ListEntry<TexTemplate> getTexTemplateEntry(Company company, String templateName) {
    return getTemplateEntry(company, templateName, getTexTemplateEntries());
  }

  List<TexTemplate> getTexTemplates(Company company) {
    return getEntries(getTexTemplateEntries(), company);
  }

  private List<ListEntry<TexTemplate>> getTexTemplateEntries() {
    if (texTemplateEntries == null) {
      setTexTemplateEntries(new ArrayList<>());
    }

    return texTemplateEntries;
  }

  private void setTexTemplateEntries(List<ListEntry<TexTemplate>> templates) {
    texTemplateEntries = templates;
  }

  private void addTexTemplate(TexTemplate template) {
    addTexTemplate(null, template);
  }

  void addTexTemplate(Company company, TexTemplate template) {
    ListEntry<TexTemplate> oldAddress = getTexTemplateEntry(company, template.getName());

    if (oldAddress == null) {
      getTexTemplateEntries().add(new ListEntry<>(company, template));
    } else {
      oldAddress.setEntry(template);
    }
  }

  void clearAllLists() {
    setAddressEntries(new ArrayList<>());
    setCompanyEntries(new ArrayList<>());
    setTemplateEntries(new ArrayList<>());
    setTexTemplateEntries(new ArrayList<>());
    setInvoices(new ArchivedInvoiceTable());
  }

  private boolean isEntryNotInList(ArchivedInvoice archivedInvoice, ArchivedInvoiceTable list) {
    return list != null && !list.contains(archivedInvoice);
  }

  private boolean hasFittingCompany(ListEntry<Address> addressEntry, Company company) {
    return hasNoCompany(addressEntry) || hasThisCompany(addressEntry, company);
  }

  private boolean hasNoCompany(ListEntry<Address> addressEntry) {
    return !addressEntry.getCompany().isPresent();
  }

  private <T> boolean hasThisCompany(ListEntry<T> entry, Company company) {
    return company.equals(entry.getCompany().orElse(null));
  }

  private boolean hasThisRecipient(ListEntry<Address> addressEntry, String recipient) {
    return addressEntry.getEntry().getRecipient().equals(recipient);
  }

  private boolean hasThisDescriptiveName(ListEntry<Company> company, String descriptiveName) {
    return descriptiveName.equals(company.getEntry().getDescriptiveName());
  }

  private <T> List<T> getEntries(List<ListEntry<T>> list, Company company) {
    return list.stream()
        .filter(entry -> isValidForCompany(entry, company))
        .map(ListEntry::getEntry)
        .collect(Collectors.toList());
  }

  private <T> boolean isValidForCompany(ListEntry<T> entry, Company company) {
    return !entry.getCompany().isPresent()
        || hasThisCompany(entry, company);
  }

  private <T extends DropListable> ListEntry<T> getTemplateEntry(Company company, String templateName, List<ListEntry<T>> list) {
    ListEntry<T> result = null;
    for (ListEntry<T> templateEntry : list) {
      if (isValidForCompany(templateEntry, company) && hasSameName(templateEntry, templateName)) {
        result = templateEntry;
      }
    }

    return result;
  }

  void addLoadable(Loadable loadable) {
    if (loadable instanceof Address) {
      addAddress(null, (Address) loadable);
    } else if (loadable instanceof ArchivedInvoice) {
      addInvoice((ArchivedInvoice) loadable);
    } else if (loadable instanceof Company) {
      addCompany((Company) loadable);
    }
  }

  private boolean hasSameName(ListEntry<? extends DropListable> templateEntry, String templateName) {
    return templateEntry.getEntry().getName().equals(templateName);
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

  private class ListEntry<T> {
    private final Company company;
    private T entry;

    ListEntry(Company aCompany, T anEntry) {
      entry = anEntry;
      company = aCompany;
    }

    ListEntry(T anEntry) {
      entry = anEntry;
      company = null;
    }

    Optional<Company> getCompany() {
      return Optional.ofNullable(company);
    }

    T getEntry() {
      return entry;
    }

    void setEntry(T anEntry) {
      entry = anEntry;
    }
  }
}
