package de.heinerion.betriebe.gui.tablemodels.archive;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.gui.tablemodels.archive.columns.*;
import de.heinerion.betriebe.models.Company;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class RechnungsListe implements TableModel {

  public static final int INDEX_NUMBER = 0;
  public static final int INDEX_RECEIVER = 1;
  public static final int INDEX_PRODUCT = 2;
  public static final int INDEX_DATE = 3;
  public static final int INDEX_SENDER = 4;
  public static final int INDEX_AMOUNT = 5;

  private static final InvoiceColumn[] COLUMNS = {
      // Spalten in Reihenfolge des Auftretens
      new NumberColumn(), new ReceiverColumn(), new ProductColumn(),
      new DateColumn(), new SenderColumn(), new AmountColumn(),};

  private static final int COLUMN_COUNT = COLUMNS.length;

  private List<ArchivedInvoice> rechnungszeilen;
  private final List<TableModelListener> tableListener = new ArrayList<>();

  /**
   * TODO elemente auf maximalZeilen begrenzen?
   */
  public RechnungsListe() {
    this.rechnungszeilen = new ArrayList<>();
  }

  /**
   * Fügt neue Rechnungsdaten ein
   *
   * @param data Die einzufügenden Daten
   */
  public void add(ArchivedInvoice data) {
    this.rechnungszeilen.add(data);
    this.notifyTableListener();
  }

  @Override
  public void addTableModelListener(TableModelListener l) {
    this.tableListener.add(l);
  }

  /**
   * Berechnet Gesamteinnahmen dieser Rechnungsliste
   *
   * @return
   */
  public double berechneEinnahmen() {
    double summe = 0;
    for (int i = 0; i < this.getAnzahlElemente(); i++) {
      summe += this.get(i).getAmount();
    }
    return summe;
  }

  public boolean contains(ArchivedInvoice data) {
    return getRechnungszeilen().contains(data);
  }

  public ArchivedInvoice get(int index) {
    return getRechnungszeilen().get(index);
  }

  public int getAnzahlElemente() {
    return (int) getRechnungszeilen()
        .stream()
        .filter(this::filterByActiveComppany)
        .count();
  }

  private List<ArchivedInvoice> getRechnungszeilen() {
    return this.rechnungszeilen.stream()
        .filter(this::filterByActiveComppany)
        .collect(Collectors.toList());
  }

  private boolean filterByActiveComppany(ArchivedInvoice invoice) {
    return invoice.getCompany().equals(Session.getActiveCompany());
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return COLUMNS[columnIndex].getClass();
  }

  @Override
  /* TODO Anzahl Zeilen */
  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return COLUMNS[columnIndex].getName();
  }

  public void getMaxNumber() {
    int nummer;
    Company company;
    for (int i = 0; i < this.getAnzahlElemente(); i++) {
      nummer = this.get(i).getInvoiceNumber();
      company = this.get(i).getCompany();

      if (company != null && nummer > company.getInvoiceNumber()) {
        company.setInvoiceNumber(nummer);
      }
    }
  }

  public File getPdfAt(int rowIndex) {
    final ArchivedInvoice data = this.get(rowIndex);
    return data.getPdf();
  }

  @Override
  public int getRowCount() {
    return getAnzahlElemente();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    final ArchivedInvoice data = this.get(rowIndex);
    return COLUMNS[columnIndex].getValue(data);
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }

  private void notifyTableListener() {
    for (TableModelListener l : this.tableListener) {
      l.tableChanged(new TableModelEvent(this));
    }
  }

  public void removeAll() {
    this.rechnungszeilen = new ArrayList<>();
  }

  @Override
  public void removeTableModelListener(TableModelListener l) {
    this.tableListener.remove(l);
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    final ArchivedInvoice data = this.get(rowIndex);
    COLUMNS[columnIndex].setValue(data, value);
  }
}
