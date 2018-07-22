package de.heinerion.betriebe.data;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.data.listable.TexTemplate;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Representation of the business class entities in memory.
 * <p>Is (for now) only to be used by {@link DataBase}.</p>
 */
final class MemoryBank {
  private final List<ListEntry<Address>> addressEntries;
  private final List<ListEntry<Company>> companyEntries;
  private final List<ListEntry<InvoiceTemplate>> templateEntries;
  private final List<ListEntry<TexTemplate>> texTemplateEntries;

  MemoryBank() {
    addressEntries = new ArrayList<>();
    companyEntries = new ArrayList<>();
    templateEntries = new ArrayList<>();
    texTemplateEntries = new ArrayList<>();
  }

  void addAddress(Company company, Address address) {
    Optional<ListEntry<Address>> oldAddress = getAddressEntry(company, address.getRecipient());

    oldAddress.ifPresent(addressEntries::remove);
    addressEntries.add(new ListEntry<>(company, address));
  }

  /**
   * Collects all {@link Address}es valid for the given {@link Company}
   *
   * @param company to be filtered by
   *
   * @return every {@link Address} connected to this {@link Company} sorted by {@link Address#getRecipient()}
   */
  List<Address> getAddresses(Company company) {
    return getSortedEntries(addressEntries, company, Address::getRecipient);
  }

  List<Address> getAllAddresses() {
    return getAllSortedEntries(addressEntries, Address::getRecipient);
  }

  /**
   * Looks for the given recipients {@link Address} in the addresses valid for the given {@link Company}.
   *
   * @param company   to be filtered by
   * @param recipient to be looked for
   *
   * @return an {@link Address} with the given recipient
   */
  Optional<Address> getAddress(Company company, String recipient) {
    return getAddressEntry(company, recipient)
        .map(ListEntry::getEntry);
  }

  private Optional<ListEntry<Address>> getAddressEntry(Company company, String recipient) {
    return addressEntries.stream()
        .filter(entry -> entry.belongsTo(company).orElse(true) && hasThisRecipient(entry, recipient))
        .findFirst();
  }

  private boolean hasThisRecipient(ListEntry<Address> addressEntry, String recipient) {
    return addressEntry.getEntry().getRecipient().equals(recipient);
  }

  void addCompany(Company company) {
    Optional<ListEntry<Company>> oldCompany = getCompanyEntry(company.getDescriptiveName());

    oldCompany.ifPresent(companyEntries::remove);
    companyEntries.add(new ListEntry<>(company));
  }

  List<Company> getAllCompanies() {
    return getAllSortedEntries(companyEntries, Company::getDescriptiveName);
  }

  Optional<Company> getCompany(String descriptiveName) {
    return getCompanyEntry(descriptiveName).map(ListEntry::getEntry);
  }

  private Optional<ListEntry<Company>> getCompanyEntry(String descriptiveName) {
    return companyEntries.stream()
        .filter(company -> company.getEntry().getDescriptiveName().equals(descriptiveName))
        .findFirst();
  }

  void addTemplate(Company company, InvoiceTemplate template) {
    Optional<ListEntry<InvoiceTemplate>> oldTemplate = getTemplateEntry(company, template.getName());

    oldTemplate.ifPresent(templateEntries::remove);
    templateEntries.add(new ListEntry<>(company, template));
  }

  List<InvoiceTemplate> getTemplates(Company company) {
    return getSortedEntries(templateEntries, company, InvoiceTemplate::getName);
  }

  private Optional<ListEntry<InvoiceTemplate>> getTemplateEntry(Company company, String name) {
    return templateEntries.stream()
        .filter(template -> template.belongsTo(company).orElse(true))
        .filter(template -> template.getEntry().getName().equals(name))
        .findFirst();
  }

  void addTexTemplate(Company company, TexTemplate template) {
    Optional<ListEntry<TexTemplate>> oldTemplate = getTexTemplateEntry(company, template.getName());

    oldTemplate.ifPresent(texTemplateEntries::remove);
    texTemplateEntries.add(new ListEntry<>(company, template));
  }

  List<TexTemplate> getTexTemplates(Company company) {
    return getSortedEntries(texTemplateEntries, company, TexTemplate::getName);
  }

  private Optional<ListEntry<TexTemplate>> getTexTemplateEntry(Company company, String name) {
    return texTemplateEntries.stream()
        .filter(template -> template.belongsTo(company).orElse(true))
        .filter(template -> template.getEntry().getName().equals(name))
        .findFirst();
  }

  private <T> List<T> getEntries(List<ListEntry<T>> list, Company company) {
    return list.stream()
        .filter(entry -> entry.belongsTo(company).orElse(true))
        .map(ListEntry::getEntry)
        .collect(Collectors.toList());
  }

  private <T> List<T> getAllEntries(List<ListEntry<T>> list) {
    return list.stream()
        .map(ListEntry::getEntry)
        .collect(Collectors.toList());
  }

  private <T, U extends Comparable<U>>
  List<T> getSortedEntries(List<ListEntry<T>> entriesList, Company company, Function<T, U> keyExtractor) {
    List<T> belongingEntries = getEntries(entriesList, Objects.requireNonNull(company));
    belongingEntries.sort(Comparator.comparing(keyExtractor));
    return Collections.unmodifiableList(belongingEntries);
  }

  private <T, U extends Comparable<U>>
  List<T> getAllSortedEntries(List<ListEntry<T>> entriesList, Function<T, U> keyExtractor) {
    List<T> belongingEntries = getAllEntries(entriesList);
    belongingEntries.sort(Comparator.comparing(keyExtractor));
    return Collections.unmodifiableList(belongingEntries);
  }

  void reset() {
    addressEntries.clear();
    companyEntries.clear();
    templateEntries.clear();
    texTemplateEntries.clear();
  }

  private final class ListEntry<T> {
    private final Company company;
    private final T entry;

    ListEntry(Company aCompany, T anEntry) {
      company = aCompany;
      entry = anEntry;
    }

    ListEntry(T anEntry) {
      company = null;
      entry = anEntry;
    }

    Optional<Company> getCompany() {
      return Optional.ofNullable(company);
    }

    T getEntry() {
      return entry;
    }

    Optional<Boolean> belongsTo(Company company) {
      return getCompany().map(c -> c.equals(company));
    }
  }
}