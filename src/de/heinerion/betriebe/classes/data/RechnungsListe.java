package de.heinerion.betriebe.classes.data;

import de.heinerion.betriebe.classes.data.invoice_table_column.*;
import de.heinerion.betriebe.models.Company;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class RechnungsListe implements TableModel {

  public static final int INDEX_NUMBER = 0;
  public static final int INDEX_RECEIVER = 1;
  public static final int INDEX_PRODUCT = 2;
  public static final int INDEX_DATE = 3;
  public static final int INDEX_SENDER = 4;
  public static final int INDEX_AMOUNT = 5;

  public static final ColumnState[] STATE = {
      // Spalten in Reihenfolge des Auftretens
      new NumberColumn(), new ReceiverColumn(), new ProductColumn(),
      new DateColumn(), new SenderColumn(), new AmountColumn(),};

  private static final int COLUMN_COUNT = STATE.length;

  private List<RechnungData> rechnungszeilen;
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
  public void add(RechnungData data) {
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

  public boolean contains(RechnungData data) {
    return this.rechnungszeilen.contains(data);
  }

  public RechnungData get(int index) {
    return this.rechnungszeilen.get(index);
  }

  public int getAnzahlElemente() {
    return this.rechnungszeilen.size();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return STATE[columnIndex].getClass();
  }

  @Override
  /* TODO Anzahl Zeilen */
  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return STATE[columnIndex].getName();
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
    final RechnungData data = this.get(rowIndex);
    return data.getPdf();
  }

  @Override
  public int getRowCount() {
    return this.getAnzahlElemente();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    final RechnungData data = this.get(rowIndex);
    return STATE[columnIndex].getValue(data);
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
    final RechnungData data = this.get(rowIndex);
    STATE[columnIndex].setValue(data, value);
  }
}
