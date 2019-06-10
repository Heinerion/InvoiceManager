package de.heinerion.invoice.tool.boundary;

import de.heinerion.invoice.tool.business.CustomerInformation;
import de.heinerion.invoice.tool.domain.*;

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
  private final Collection<Product> products = new HashSet<>();
  private Collection<Company> companies = new HashSet<>();

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

  public void save(Product product) {
    products.add(product);
  }

  public Collection<Product> getProducts() {
    return products;
  }

  public Optional<Product> getProduct(String name) {
    return products.stream().findFirst();
  }

  public boolean delete(Product product) {
    return products.remove(product);
  }

  public void save(Company company) {
    companies.add(company);
  }

  public Collection<Company> getCompanys() {
    return companies;
  }

  public Optional<Company> getCompany(String name) {
    return companies.stream().findFirst();
  }

  public boolean delete(Company company) {
    return companies.remove(company);
  }
}
