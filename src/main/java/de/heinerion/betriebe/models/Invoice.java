package de.heinerion.betriebe.models;

import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.tools.PathTools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice extends Letter {
  private static final int PERCENT = 100;

  private final int number;

  private final List<Item> items;

  private double net;
  private final double vat;
  private double tax;
  private double gross;

  public Invoice(LocalDate aDate, Company theSender, Address theReceiver) {
    super(aDate, theSender, theReceiver);
    subject = Translator.translate("invoice.title");

    this.items = new ArrayList<>();

    this.vat = theSender.getValueAddedTax();
    this.updateValues();

    this.number = theSender.getInvoiceNumber();
  }

  public void add(String artikel, String einheit, double preis, double anzahl) {
    this.addItem(new Item(new Product(artikel, einheit, preis), anzahl));
  }

  private void addItem(Item item) {
    this.items.add(item);
    this.updateValues();
  }

  // TODO removeItem?

  @Override
  // TODO Classification war wofür?
  public String[] getClassification() {
    return new String[]{this.company.getDescriptiveName(),
        PathTools.determineFolderName(this.getClass()),};
  }

  @Override
  public String getEntryName() {
    return this.number + " " + this.receiver.getRecipient();
  }

  public double getGross() {
    return this.gross;
  }

  public List<Item> getItems() {
    return this.items;
  }

  public double getNet() {
    return this.net;
  }

  public int getNumber() {
    return this.number;
  }

  public double getTax() {
    return this.tax;
  }

  public double getVat() {
    return this.vat;
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
}
