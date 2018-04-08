package de.heinerion.betriebe.data;

import de.heinerion.betriebe.data.listable.DropListable;
import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.data.listable.TexTemplate;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.storage.loading.IO;
import de.heinerion.invoice.storage.loading.Loadable;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoiceTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class DataBase {
  private static final Logger logger = LogManager.getLogger(DataBase.class);

  private List<ListEntry<Address>> addressEntries;
  private List<ListEntry<Company>> companyEntries;
  private List<ListEntry<InvoiceTemplate>> templateEntries;
  private List<ListEntry<TexTemplate>> texTemplateEntries;

  private ArchivedInvoiceTable invoices;

  private IO io;

  private static DataBase instance = new DataBase();

  private DataBase() {
  }

  public static DataBase getInstance() {
    return instance;
  }

  public List<Address> getAddresses(Company company) {
    List<Address> ret = getEntries(getAddressEntries(), company);
    ret.sort((a, b) -> Collator.getInstance().compare(a.getRecipient(), b.getRecipient()));
    return ret;
  }

  public void setIo(IO io) {
    this.io = io;
  }

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
    result.sort((a, b) -> Collator.getInstance().compare(a.getDescriptiveName(), b.getDescriptiveName()));
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

  public void addTemplate(Company company, InvoiceTemplate template) {
    ListEntry<InvoiceTemplate> oldAddress = getTemplateEntry(company, template.getName());

    if (oldAddress == null) {
      getTemplateEntries().add(new ListEntry<>(company, template));
    } else {
      oldAddress.setEntry(template);
    }
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

  public void addTexTemplate(Company company, TexTemplate template) {
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

  public void addLoadable(Loadable loadable) {
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

  public void removeAllInvoices() {
    setInvoices(new ArchivedInvoiceTable());
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
