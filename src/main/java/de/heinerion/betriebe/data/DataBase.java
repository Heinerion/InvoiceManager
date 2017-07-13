package de.heinerion.betriebe.data;

import de.heinerion.betriebe.data.listable.DropListable;
import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.data.listable.TexTemplate;
import de.heinerion.betriebe.fileoperations.IO;
import de.heinerion.betriebe.fileoperations.loading.Loadable;
import de.heinerion.betriebe.gui.tablemodels.archive.ArchivedInvoice;
import de.heinerion.betriebe.gui.tablemodels.archive.ArchivedInvoiceTable;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class DataBase {
  private static final Logger logger = LogManager.getLogger(DataBase.class);

  private static List<ListEntry<Address>> addresses = new ArrayList<>();
  private static List<ListEntry<Company>> companies = new ArrayList<>();
  private static List<ListEntry<InvoiceTemplate>> templates = new ArrayList<>();
  private static List<ListEntry<TexTemplate>> texTemplates = new ArrayList<>();

  private static ArchivedInvoiceTable invoices = new ArchivedInvoiceTable();

  static {
    new DataBase();
  }

  private DataBase() {
    initLists();
  }

  private static void initLists() {
    addresses = new ArrayList<>();
    companies = new ArrayList<>();
    templates = new ArrayList<>();
    texTemplates = new ArrayList<>();

    removeAllInvoices();
  }

  static void addAddress(Company company, Address address) {
    ListEntry<Address> oldAddress = getAddressEntry(company, address.getRecipient());

    if (oldAddress == null) {
      addresses.add(new ListEntry<>(company, address));
    } else {
      oldAddress.setEntry(address);
    }
  }

  public static void addTemplate(Company company, InvoiceTemplate template) {
    ListEntry<InvoiceTemplate> oldAddress = getTemplateEntry(company, template.getName());

    if (oldAddress == null) {
      templates.add(new ListEntry<>(company, template));
    } else {
      oldAddress.setEntry(template);
    }
  }

  public static void addTexTemplate(Company company, TexTemplate template) {
    ListEntry<TexTemplate> oldAddress = getTexTemplateEntry(company, template.getName());

    if (oldAddress == null) {
      texTemplates.add(new ListEntry<>(company, template));
    } else {
      oldAddress.setEntry(template);
    }
  }

  static void addCompany(Company company) {
    Session.addAvailableCompany(company);

    ListEntry<Company> oldCompany = getCompanyEntry(company.getDescriptiveName());

    if (oldCompany == null) {
      companies.add(new ListEntry<>(null, company));
    } else {
      oldCompany.setEntry(company);
    }
  }

  public static void addAddress(Address address) {
    addAddress(null, address);

    IO.saveAddresses();
  }

  static void addInvoice(ArchivedInvoice archivedInvoice) {
    if (isEntryNotInList(archivedInvoice, invoices)) {
      invoices.add(archivedInvoice);
    }
  }

  private static boolean isEntryNotInList(ArchivedInvoice archivedInvoice, ArchivedInvoiceTable list) {
    return list != null && !list.contains(archivedInvoice);
  }

  public static Address getAddress(Company company, String recipient) {
    Address result = null;

    if (logger.isDebugEnabled()) {
      logger.debug("getAddress({}, {})", company, recipient);
    }

    ListEntry<Address> entry = getAddressEntry(company, recipient);
    if (entry != null) {
      result = entry.getEntry();
    }

    return result;
  }

  private static ListEntry<Address> getAddressEntry(Company company, String recipient) {
    ListEntry<Address> result = null;

    for (ListEntry<Address> address : addresses) {
      if (hasFittingCompany(address, company) && hasThisRecipient(address, recipient)) {
        result = address;
        break;
      }
    }

    return result;
  }

  private static boolean hasFittingCompany(ListEntry<Address> addressEntry, Company company) {
    return hasNoCompany(addressEntry) || hasThisCompany(addressEntry, company);
  }

  private static boolean hasNoCompany(ListEntry<Address> addressEntry) {
    return addressEntry.getCompany() == null;
  }

  private static boolean hasThisCompany(ListEntry<Address> addressEntry, Company company) {
    return addressEntry.getCompany().equals(company);
  }

  private static boolean hasThisRecipient(ListEntry<Address> addressEntry, String recipient) {
    return addressEntry.getEntry().getRecipient().equals(recipient);
  }

  public static Company getCompany(String descriptiveName) {
    Company result = null;

    if (logger.isDebugEnabled()) {
      logger.debug("getCompany({})", descriptiveName);
    }

    ListEntry<Company> entry = getCompanyEntry(descriptiveName);
    if (entry != null) {
      result = entry.getEntry();
    }

    return result;
  }

  private static ListEntry<Company> getCompanyEntry(String descriptiveName) {
    ListEntry<Company> result = null;

    for (ListEntry<Company> company : companies) {
      if (hasThisDescriptiveName(company, descriptiveName)) {
        result = company;
        break;
      }
    }

    return result;
  }

  private static boolean hasThisDescriptiveName(ListEntry<Company> company, String descriptiveName) {
    return descriptiveName.equals(company.getEntry().getDescriptiveName());
  }

  public static List<Address> getAddresses() {
    return getAddresses(null);
  }

  static List<Company> getCompanies() {
    List<Company> result = getEntries(companies, null);
    result.sort((a, b) -> Collator.getInstance().compare(a.getDescriptiveName(), b.getDescriptiveName()));
    return result;
  }

  public static List<Address> getAddresses(Company company) {
    List<Address> ret = getEntries(addresses, company);
    ret.sort((a, b) -> Collator.getInstance().compare(a.getRecipient(), b.getRecipient()));
    return ret;
  }

  private static <T> List<T> getEntries(List<ListEntry<T>> list, Company company) {

    return list.stream()
        .filter(entry -> isValidForCompany(entry, company))
        .map(ListEntry::getEntry)
        .collect(Collectors.toList());
  }

  private static <T> boolean isValidForCompany(ListEntry<T> entry, Company company) {
    return entry.getCompany() == null || entry.getCompany().equals(company);
  }

  public static ArchivedInvoiceTable getInvoices() {
    return invoices;
  }

  private static ListEntry<InvoiceTemplate> getTemplateEntry(Company company, String templateName) {
    return getTemplateEntry(company, templateName, templates);
  }

  private static ListEntry<TexTemplate> getTexTemplateEntry(Company company, String templateName) {
    return getTemplateEntry(company, templateName, texTemplates);
  }

  private static <T extends DropListable> ListEntry<T> getTemplateEntry(Company company, String templateName,
                                                                        List<ListEntry<T>> list) {
    ListEntry<T> result = null;
    for (ListEntry<T> templateEntry : list) {
      if (isValidCompany(company, templateEntry) && hasSameName(templateEntry, templateName)) {
        result = templateEntry;
      }
    }

    return result;
  }

  public static List<InvoiceTemplate> getTemplates(Company company) {
    return getEntries(templates, company);
  }

  public static List<TexTemplate> getTexTemplates(Company company) {
    return getEntries(texTemplates, company);
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

  private static boolean isValidCompany(Company company, ListEntry<? extends DropListable> templateEntry) {
    return templateEntry.getCompany() == null
        || templateEntry.getCompany().equals(company);
  }

  private static boolean hasSameName(ListEntry<? extends DropListable> templateEntry, String templateName) {
    return templateEntry.getEntry().getName().equals(templateName);
  }

  public static void removeAllInvoices() {
    invoices = new ArchivedInvoiceTable();
  }

  public static void clearAllLists() {
    initLists();
  }

  private static class ListEntry<T> {
    private T entry;
    private final Company company;

    ListEntry(Company aCompany, T anEntry) {
      entry = anEntry;
      company = aCompany;
    }

    public Company getCompany() {
      return company;
    }

    T getEntry() {
      return entry;
    }

    void setEntry(T anEntry) {
      entry = anEntry;
    }
  }
}
