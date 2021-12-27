package de.heinerion.betriebe.models;

import de.heinerion.invoice.Translator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Invoice extends Letter {
  private static final int PERCENT = 100;

  private int number;

  private List<Item> items;

  private double net;
  private double vat;
  private double tax;
  private double gross;

  /**
   * For persistence only
   */
  public Invoice() {
    super();
  }

  public Invoice(LocalDate aDate, Company theSender, Address theReceiver) {
    super(aDate, theSender, theReceiver);
    subject = Translator.translate("invoice.title");

    this.items = new ArrayList<>();

    this.vat = theSender.getValueAddedTax();
    this.updateValues();

    this.number = theSender.getInvoiceNumber();
  }

  public void add(String article, String unit, double price, double count) {
    this.addItem(new Item(article, unit, price, count));
  }

  @Override
  public void addMessageLine(String messageLine) {
    add(messageLine, null, 0, 0);
  }

  private void addItem(Item item) {
    this.items.add(item);
    this.updateValues();
  }

  // TODO removeItem?

  @Override
  public String getEntryName() {
    return this.number + " " + this.receiver.getRecipient();
  }

  public double getGross() {
    return this.gross;
  }

  public void setGross(double gross) {
    this.gross = gross;
  }

  public List<Item> getItems() {
    return this.items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public double getNet() {
    return this.net;
  }

  public void setNet(double net) {
    this.net = net;
  }

  public int getNumber() {
    return this.number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public double getTax() {
    return this.tax;
  }

  public void setTax(double tax) {
    this.tax = tax;
  }

  public double getVat() {
    return this.vat;
  }

  public void setVat(double vat) {
    this.vat = vat;
  }

  @Override
  public boolean isPrintable() {
    return true;
  }

  private void updateValues() {
    this.net = 0;
    for (Item item : this.getItems()) {
      this.net += item.getTotal();
    }
    this.tax = this.net * this.vat / PERCENT;
    this.gross = this.net + this.tax;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
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
    return "Invoice{" +
        "number=" + number +
        ", items=" + items +
        ", net=" + net +
        ", vat=" + vat +
        ", tax=" + tax +
        ", gross=" + gross +
        ", subject='" + subject + '\'' +
        ", receiver=" + receiver +
        '}';
  }
}
