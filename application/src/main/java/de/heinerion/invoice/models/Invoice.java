package de.heinerion.invoice.models;

import de.heinerion.contract.Contract;
import de.heinerion.invoice.Translator;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor()
@Entity
@Table(name = "invoice")
public class Invoice implements Conveyable<Invoice> {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private static final int PERCENT = 100;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;
  private LocalDate date;
  @ManyToOne
  @JoinColumn(name = "receiver_id")
  private Address receiver;
  private int number;

  @OneToMany
  @JoinTable(name = "invoice_items",
      joinColumns = @JoinColumn(name = "invoice_id"),
      inverseJoinColumns = @JoinColumn(name = "item_id"))
  private Set<Item> items = new HashSet<>();
  private double vat;

  private double net;
  private double tax;
  private double gross;

  public Invoice(LocalDate invoiceDate, Company sender, Address receiversAddress, int invoiceNumber) {
    date = invoiceDate;
    company = sender;
    receiver = receiversAddress;
    vat = sender.getValueAddedTax();
    updateValues();
    number = invoiceNumber;
  }

  @Override
  public String getSubject() {
    return Translator.translate("invoice.title");
  }

  public void add(String article, String unit, double price, double count) {
    items.add(Item.of(items.size(), article, unit, price, count));
    updateValues();
  }

  public void add(String message) {
    items.add(Item.of(items.size(), message));
    updateValues();
  }

  public void addMessageLine(String messageLine) {
    add(messageLine);
  }

  public List<Item> getItems() {
    return items.stream()
        .sorted(Comparator.nullsLast(Comparator.comparing(Item::getPosition, Integer::compareTo)))
        .toList();
  }

  public Invoice setItems(List<Item> items) {
    Contract.requireNotNull(items, "items");
    this.items = updateItemPositions(items);
    return this;
  }

  private Set<Item> updateItemPositions(List<Item> items) {
    Contract.requireNotNull(items, "items");
    Set<Item> set = new HashSet<>();
    for (Item item : items) {
      // Items could've been reordered
      set.add(updatePosition(item, items.indexOf(item)));
    }
    return set;
  }

  private Item updatePosition(Item item, int newPosition) {
    Contract.requireNotNull(item, "item");
    return Objects.equals(item.getPosition(), newPosition)
        ? item
        : item.setPosition(newPosition);
  }

  // TODO removeItem?

  @Override
  public boolean isPrintable() {
    return true;
  }

  private void updateValues() {
    net = items.stream()
        .map(Item::getTotal)
        .filter(Objects::nonNull)
        .reduce(Double::sum)
        .orElse(0d);

    tax = net * vat / PERCENT;
    gross = net + tax;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Invoice invoice = (Invoice) o;
    return number == invoice.number &&
        Double.compare(invoice.net, net) == 0 &&
        Double.compare(invoice.vat, vat) == 0 &&
        Double.compare(invoice.tax, tax) == 0 &&
        Double.compare(invoice.gross, gross) == 0 &&
        Objects.equals(items, invoice.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, items, net, vat, tax, gross);
  }

  @Override
  public String toString() {
    return "Invoice{number=%d, receiver=%s}".formatted(number, receiver);
  }
}
