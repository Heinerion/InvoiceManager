package de.heinerion.invoice.tool;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class DataStore {
  private Collection<Customer> customers = new HashSet<>();
  private Collection<Invoice> invoices = new HashSet<>();
  private Collection<Letter> letters = new HashSet<>();

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
}
