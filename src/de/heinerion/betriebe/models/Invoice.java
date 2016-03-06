package de.heinerion.betriebe.models;

import de.heinerion.betriebe.enums.Utilities;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.models.interfaces.Storable;
import de.heinerion.betriebe.tools.PathTools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Invoice implements Conveyable, Storable {
  private static final int PERCENT = 100;
  private final Company company;
  private final LocalDate date;
  private final int number;

  private final List<Item> items;

  private double net;
  private final double vat;
  private double tax;
  private double gross;
  private final Address receiver;

  public Invoice(LocalDate aDate, Company theSender, Address theReceiver) {
    this.date = aDate;
    this.company = theSender;
    this.receiver = theReceiver;

    this.items = new ArrayList<>();

    this.vat = theSender.getValueAddedTax();
    this.updateValues();

    this.number = theSender.getInvoiceNumber();
  }

  public void add(String artikel, String einheit, double preis, double anzahl) {
    this.addItem(new Item(new Product(artikel, einheit, preis), anzahl));
  }

  public void addItem(Item item) {
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
  public Company getCompany() {
    return this.company;
  }

  @Override
  public LocalDate getDate() {
    return this.date;
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

  @Override
  public Address getReceiver() {
    return this.receiver;
  }

  @Override
  public String getSubject() {
    return Utilities.RECHNUNG.getText();
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
