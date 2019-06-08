package de.heinerion.invoice.tool.boundary;

import de.heinerion.invoice.tool.business.CustomerInformation;
import de.heinerion.invoice.tool.domain.Customer;
import de.heinerion.invoice.tool.domain.Invoice;
import de.heinerion.invoice.tool.domain.Letter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Access layer for data storage and retrieval
 */
public class DataStore {
  private final Collection<Customer> customers = new HashSet<>();
  private final Collection<Invoice> invoices = new HashSet<>();
  private final Collection<Letter> letters = new HashSet<>();

  public void save(Customer customer) {
    customers.add(customer);
  }

  public Collection<Customer> getCustomers() {
    return customers;
  }

  public Optional<Customer> getCustomer(String name) {
    return customers.stream().findFirst();
  }

  public boolean delete(Customer customer) {
    return customers.remove(customer);
  }

  public void save(Invoice invoice) {
    invoices.add(invoice);
  }

  public Collection<Invoice> getInvoices() {
    return invoices;
  }

  public Optional<Invoice> getInvoice(String id) {
    return invoices.stream().findFirst();
  }

  public void save(Letter letter) {
    letters.add(letter);
  }

  public Collection<Letter> getLetters() {
    return letters;
  }

  public Optional<Letter> getLetter() {
    return letters.stream().findFirst();
  }

  public CustomerInformation getCustomerInformation(Customer customer) {
    return new CustomerInformation(getLetters(customer), getInvoices(customer));
  }

  private Collection<Letter> getLetters(Customer customer) {
    return letters.stream()
        .filter(letter -> Objects.equals(customer, letter.getCustomer()))
        .collect(Collectors.toSet());
  }

  private Collection<Invoice> getInvoices(Customer customer) {
    return invoices.stream()
        .filter(invoice -> Objects.equals(customer, invoice.getCustomer()))
        .collect(Collectors.toSet());
  }
}
