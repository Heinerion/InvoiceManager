package de.heinerion.betriebe.data;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.models.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

/**
 * Representation of the business class entities in memory.
 * <p>Is (for now) only to be used by {@link DataBase}.</p>
 */
@Service
@RequiredArgsConstructor
public
class MemoryBank {
  private final List<ListEntry<InvoiceTemplate>> templateEntries = new ArrayList<>();

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

  private <T> List<T> getEntries(List<ListEntry<T>> list, Company company) {
    return list.stream()
        .filter(entry -> entry.belongsTo(company).orElse(true))
        .map(ListEntry::getEntry)
        .toList();
  }
  
  private <T, U extends Comparable<U>>
  List<T> getSortedEntries(List<ListEntry<T>> entriesList, Company company, Function<T, U> keyExtractor) {
    return getEntries(entriesList, Objects.requireNonNull(company)).stream()
        .sorted(Comparator.comparing(keyExtractor))
        .toList();
  }

  void reset() {
    templateEntries.clear();
  }

  private static final class ListEntry<T> {
    private final Company company;
    private final T entry;

    ListEntry(Company aCompany, T anEntry) {
      company = aCompany;
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
