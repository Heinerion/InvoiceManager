package de.heinerion.betriebe.data;

import de.heinerion.betriebe.classes.data.RechnungData;
import de.heinerion.betriebe.classes.data.RechnungsListe;
import de.heinerion.betriebe.classes.data.TexVorlage;
import de.heinerion.betriebe.classes.file_operations.IO;
import de.heinerion.betriebe.classes.file_operations.loading.Loadable;
import de.heinerion.betriebe.classes.texting.Vorlage;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataBase {
  private static Logger logger = LogManager.getLogger(DataBase.class);

  private static List<ListEntry<Address>> addresses = new ArrayList<>();
  private static List<ListEntry<Company>> companies = new ArrayList<>();

  private static List<ListEntry<Vorlage>> templates = new ArrayList<>();

  private static List<ListEntry<TexVorlage>> texTemplates = new ArrayList<>();

  /**
   * beinhaltet alle Rechnungen TODO wozu RechnungsListe?
   */
  private static Map<Company, RechnungsListe> rechnungen = new HashMap<>();

  private DataBase() {
  }

  public static void addAddress(Company company, Address address) {
    final ListEntry<Address> oldAddress = getAddressEntry(company,
        address.getRecipient());

    if (oldAddress == null) {
      addresses.add(new ListEntry<>(company, address));
    } else {
      oldAddress.setEntry(address);
    }
  }

  public static void addCompany(Company company) {
    Session.addAvailableCompany(company);
  }

  public static void addAdresse(Address address) {
    addAddress(null, address);

    IO.saveAddresses();
  }

  public static void addRechnung(RechnungData daten) {
    final RechnungsListe liste = getRechnungen(Session.getActiveCompany());

    if (liste != null && !liste.contains(daten)) {
      liste.add(daten);
    }
  }

  public static void addTemplate(Company company, Vorlage template) {
    final ListEntry<Vorlage> oldAddress = getTemplateEntry(company,
        template.getName());

    if (oldAddress == null) {
      templates.add(new ListEntry<>(company, template));
    } else {
      oldAddress.setEntry(template);
    }
  }

  public static void addTexTemplate(Company company, TexVorlage template) {
    final ListEntry<TexVorlage> oldAddress = getTexTemplateEntry(company,
        template.getName());

    if (oldAddress == null) {
      texTemplates.add(new ListEntry<>(company, template));
    } else {
      oldAddress.setEntry(template);
    }
  }

  public static Address getAddress(Company company, String recipient) {
    if (logger.isDebugEnabled()) {
      logger.debug("getAddress({}, {})", company, recipient);
    }
    final ListEntry<Address> result = getAddressEntry(company, recipient);

    if (result != null) {
      return result.getEntry();
    } else {
      return null;
    }
  }

  private static ListEntry<Address> getAddressEntry(Company company,
                                                    String recipient) {
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

  public static List<Address> getAddresses() {
    return getAddresses(null);
  }

  public static List<Address> getAddresses(Company company) {
    final List<Address> ret = getEntries(addresses, company);
    ret.sort((a, b) -> Collator.getInstance().compare(a.getRecipient(),
        b.getRecipient()));
    return ret;
  }

  private static <T> List<T> getEntries(List<ListEntry<T>> list, Company company) {
    final List<T> result = new ArrayList<>();

    for (final ListEntry<T> entry : list) {
      if (entry.getCompany() == null || entry.getCompany().equals(company)) {
        result.add(entry.getEntry());
      }
    }

    return result;
  }

  public static RechnungsListe getRechnungen() {
    return getRechnungen(Session.getActiveCompany());
  }

  public static RechnungsListe getRechnungen(Company company) {
    RechnungsListe result = null;
    if (company != null) {
      result = rechnungen.get(company);
    }

    if (result == null) {
      result = new RechnungsListe();
    }

    return result;
  }

  public static Vorlage getTemplate(Company company, String name) {
    final ListEntry<Vorlage> result = getTemplateEntry(company, name);

    if (result != null) {
      return result.getEntry();
    } else {
      return null;
    }
  }

  private static ListEntry<Vorlage> getTemplateEntry(Company company,
                                                     String templateName) {
    for (final ListEntry<Vorlage> templateEntry : templates) {
      if (templateEntry.getCompany() == null
          || templateEntry.getCompany().equals(company)) {
        if (templateEntry.getEntry().getName().equals(templateName)) {
          return templateEntry;
        }
      }
    }

    return null;
  }

  public static List<Vorlage> getTemplates(Company company) {
    return getEntries(templates, company);
  }

  public static TexVorlage getTexTemplate(Company company, String name) {
    final ListEntry<TexVorlage> result = getTexTemplateEntry(company, name);

    if (result != null) {
      return result.getEntry();
    } else {
      return null;
    }
  }

  public static void addLoadable(Loadable loadable) {
    if (loadable instanceof Address) {
      addAddress(null, (Address) loadable);
    } else if (loadable instanceof RechnungData) {
      addRechnung((RechnungData) loadable);
    } else if (loadable instanceof Company) {
      addCompany((Company) loadable);
    }
  }

  private static ListEntry<TexVorlage> getTexTemplateEntry(Company company,
                                                           String templateName) {
    for (final ListEntry<TexVorlage> templateEntry : texTemplates) {
      if (templateEntry.getCompany() == null
          || templateEntry.getCompany().equals(company)) {
        if (templateEntry.getEntry().getName().equals(templateName)) {
          return templateEntry;
        }
      }
    }

    return null;
  }

  public static List<TexVorlage> getTexTemplates(Company company) {
    return getEntries(texTemplates, company);
  }

  public static void removeAllInvoices() {
    rechnungen = new HashMap<>();
    for (final Company c : Session.getAvailableCompanies()) {
      rechnungen.put(c, new RechnungsListe());
    }
  }

  private static class ListEntry<T> {
    private T entry;
    private final Company company;

    public ListEntry(Company aCompany, T anEntry) {
      this.entry = anEntry;
      this.company = aCompany;
    }

    public Company getCompany() {
      return this.company;
    }

    public T getEntry() {
      return this.entry;
    }

    public void setEntry(T anEntry) {
      this.entry = anEntry;
    }
  }
}
