package de.heinerion.betriebe.data;

import de.heinerion.betriebe.data.listable.DropListable;
import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.data.listable.TexTemplate;
import de.heinerion.betriebe.loading.IO;
import de.heinerion.betriebe.loading.Loadable;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.view.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.betriebe.view.menu.tablemodels.archive.ArchivedInvoiceTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class DataBase {
  private static final Logger logger = LogManager.getLogger(DataBase.class);

  private static List<ListEntry<Address>> addressEntries;
  private static List<ListEntry<Company>> companyEntries;
  private static List<ListEntry<InvoiceTemplate>> templateEntries;
  private static List<ListEntry<TexTemplate>> texTemplateEntries;

  private static ArchivedInvoiceTable invoices;

  private static IO io;

  private DataBase() {
  }

  public static void setIo(IO io) {
    DataBase.io = io;
  }

  public static Optional<Address> getAddress(Company company, String recipient) {
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

  private static ListEntry<Address> getAddressEntry(Company company, String recipient) {
    ListEntry<Address> result = null;

    for (ListEntry<Address> address : getAddressEntries()) {
      if (hasFittingCompany(address, company) && hasThisRecipient(address, recipient)) {
        result = address;
        break;
      }
    }

    return result;
  }

  public static List<Address> getAddresses() {
    return getAddresses(null);
  }

  public static List<Address> getAddresses(Company company) {
    List<Address> ret = getEntries(getAddressEntries(), company);
    ret.sort((a, b) -> Collator.getInstance().compare(a.getRecipient(), b.getRecipient()));
    return ret;
  }

  private static List<ListEntry<Address>> getAddressEntries() {
    if (DataBase.addressEntries == null) {
      setAddressEntries(new ArrayList<>());
    }

    return DataBase.addressEntries;
  }

  private static void setAddressEntries(ArrayList<ListEntry<Address>> addresses) {
    DataBase.addressEntries = addresses;
  }

  static void addAddress(Company company, Address address) {
    ListEntry<Address> oldAddress = getAddressEntry(company, address.getRecipient());

    if (oldAddress == null) {
      getAddressEntries().add(new ListEntry<>(company, address));
    } else {
      oldAddress.setEntry(address);
    }
  }

  public static void addAddress(Address address) {
    addAddress(null, address);

    io.saveAddresses();
  }

  static Optional<Company> getCompany(String descriptiveName) {
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

  private static ListEntry<Company> getCompanyEntry(String descriptiveName) {
    ListEntry<Company> result = null;

    for (ListEntry<Company> company : getCompanyEntries()) {
      if (hasThisDescriptiveName(company, descriptiveName)) {
        result = company;
        break;
      }
    }

    return result;
  }

  static List<Company> getCompanies() {
    List<Company> result = getEntries(getCompanyEntries(), null);
    result.sort((a, b) -> Collator.getInstance().compare(a.getDescriptiveName(), b.getDescriptiveName()));
    return result;
  }

  private static List<ListEntry<Company>> getCompanyEntries() {
    if (companyEntries == null) {
      setCompanyEntries(new ArrayList<>());
    }

    return companyEntries;
  }

  private static void setCompanyEntries(List<ListEntry<Company>> companies) {
    companyEntries = companies;
  }

  static void addCompany(Company company) {
    Session.addAvailableCompany(company);

    ListEntry<Company> oldCompany = getCompanyEntry(company.getDescriptiveName());

    if (oldCompany == null) {
      getCompanyEntries().add(new ListEntry<>(company));
    } else {
      oldCompany.setEntry(company);
    }
  }

  public static ArchivedInvoiceTable getInvoices() {
    if (invoices == null) {
      setInvoices(new ArchivedInvoiceTable());
    }

    return invoices;
  }

  private static void setInvoices(ArchivedInvoiceTable archivedInvoices) {
    invoices = archivedInvoices;
  }

  static void addInvoice(ArchivedInvoice archivedInvoice) {
    if (isEntryNotInList(archivedInvoice, getInvoices())) {
      getInvoices().add(archivedInvoice);
    }
  }

  private static ListEntry<InvoiceTemplate> getTemplateEntry(Company company, String templateName) {
    return getTemplateEntry(company, templateName, getTemplateEntries());
  }

  public static List<InvoiceTemplate> getTemplates(Company company) {
    return getEntries(getTemplateEntries(), company);
  }

  private static List<ListEntry<InvoiceTemplate>> getTemplateEntries() {
    if (templateEntries == null) {
      setTemplateEntries(new ArrayList<>());
    }

    return templateEntries;
  }

  private static void setTemplateEntries(List<ListEntry<InvoiceTemplate>> templates) {
    templateEntries = templates;
  }

  public static void addTemplate(Company company, InvoiceTemplate template) {
    ListEntry<InvoiceTemplate> oldAddress = getTemplateEntry(company, template.getName());

    if (oldAddress == null) {
      getTemplateEntries().add(new ListEntry<>(company, template));
    } else {
      oldAddress.setEntry(template);
    }
  }

  private static ListEntry<TexTemplate> getTexTemplateEntry(Company company, String templateName) {
    return getTemplateEntry(company, templateName, getTexTemplateEntries());
  }

  static List<TexTemplate> getTexTemplates(Company company) {
    return getEntries(getTexTemplateEntries(), company);
  }

  private static List<ListEntry<TexTemplate>> getTexTemplateEntries() {
    if (texTemplateEntries == null) {
      setTexTemplateEntries(new ArrayList<>());
    }

    return texTemplateEntries;
  }

  private static void setTexTemplateEntries(List<ListEntry<TexTemplate>> templates) {
    texTemplateEntries = templates;
  }

  public static void addTexTemplate(Company company, TexTemplate template) {
    ListEntry<TexTemplate> oldAddress = getTexTemplateEntry(company, template.getName());

    if (oldAddress == null) {
      getTexTemplateEntries().add(new ListEntry<>(company, template));
    } else {
      oldAddress.setEntry(template);
    }
  }

  static void clearAllLists() {
    setAddressEntries(new ArrayList<>());
    setCompanyEntries(new ArrayList<>());
    setTemplateEntries(new ArrayList<>());
    setTexTemplateEntries(new ArrayList<>());
    setInvoices(new ArchivedInvoiceTable());
  }

  private static boolean isEntryNotInList(ArchivedInvoice archivedInvoice, ArchivedInvoiceTable list) {
    return list != null && !list.contains(archivedInvoice);
  }

  private static boolean hasFittingCompany(ListEntry<Address> addressEntry, Company company) {
    return hasNoCompany(addressEntry) || hasThisCompany(addressEntry, company);
  }

  private static boolean hasNoCompany(ListEntry<Address> addressEntry) {
    return !addressEntry.getCompany().isPresent();
  }

  private static <T> boolean hasThisCompany(ListEntry<T> entry, Company company) {
    return company.equals(entry.getCompany().orElse(null));
  }

  private static boolean hasThisRecipient(ListEntry<Address> addressEntry, String recipient) {
    return addressEntry.getEntry().getRecipient().equals(recipient);
  }

  private static boolean hasThisDescriptiveName(ListEntry<Company> company, String descriptiveName) {
    return descriptiveName.equals(company.getEntry().getDescriptiveName());
  }

  private static <T> List<T> getEntries(List<ListEntry<T>> list, Company company) {
    return list.stream()
        .filter(entry -> isValidForCompany(entry, company))
        .map(ListEntry::getEntry)
        .collect(Collectors.toList());
  }

  private static <T> boolean isValidForCompany(ListEntry<T> entry, Company company) {
    return !entry.getCompany().isPresent()
        || hasThisCompany(entry, company);
  }

  private static <T extends DropListable> ListEntry<T> getTemplateEntry(Company company, String templateName, List<ListEntry<T>> list) {
    ListEntry<T> result = null;
    for (ListEntry<T> templateEntry : list) {
      if (isValidForCompany(templateEntry, company) && hasSameName(templateEntry, templateName)) {
        result = templateEntry;
      }
    }

    return result;
  }

  public static void addLoadable(Loadable loadable) {
    if (loadable instanceof Address) {
      addAddress(null, (Address) loadable);
    } else if (loadable instanceof ArchivedInvoice) {
      addInvoice((ArchivedInvoice) loadable);
    } else if (loadable instanceof Company) {
      addCompany((Company) loadable);
    }
  }

  private static boolean hasSameName(ListEntry<? extends DropListable> templateEntry, String templateName) {
    return templateEntry.getEntry().getName().equals(templateName);
  }

  public static void removeAllInvoices() {
    setInvoices(new ArchivedInvoiceTable());
  }

  private static class ListEntry<T> {
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

    public Optional<Company> getCompany() {
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
