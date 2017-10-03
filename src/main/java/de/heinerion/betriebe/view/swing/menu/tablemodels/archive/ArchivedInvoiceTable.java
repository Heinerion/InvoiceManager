package de.heinerion.betriebe.view.swing.menu.tablemodels.archive;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.view.swing.menu.tablemodels.archive.columns.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ArchivedInvoiceTable implements TableModel {

  public static final int INDEX_NUMBER = 0;
  public static final int INDEX_RECEIVER = 1;
  public static final int INDEX_PRODUCT = 2;
  public static final int INDEX_DATE = 3;
  public static final int INDEX_SENDER = 4;
  public static final int INDEX_AMOUNT = 5;

  private static final InvoiceColumn[] COLUMNS = {
      // columns in order of appearance
      new NumberColumn(), new ReceiverColumn(), new ProductColumn(),
      new DateColumn(), new SenderColumn(), new AmountColumn(),};

  private static final int COLUMN_COUNT = COLUMNS.length;

  private List<ArchivedInvoice> invoiceList;
  private final List<TableModelListener> tableListener = new ArrayList<>();

  public ArchivedInvoiceTable() {
    this.invoiceList = new ArrayList<>();
  }

  /**
   * Add a new archived invoice
   *
   * @param data the archived invoice to be added
   */
  public void add(ArchivedInvoice data) {
    this.invoiceList.add(data);
    this.notifyTableListener();
  }

  @Override
  public void addTableModelListener(TableModelListener l) {
    this.tableListener.add(l);
  }

  /**
   * calculates the total sum of all shown items
   *
   * @return cumulated sum of all items
   */
  public double calculateRevenues() {
    double sum = 0;
    for (int i = 0; i < this.getItemCount(); i++) {
      sum += this.get(i).getAmount();
    }
    return sum;
  }

  public boolean contains(ArchivedInvoice data) {
    return getInvoiceList().contains(data);
  }

  public ArchivedInvoice get(int index) {
    return getInvoiceList().get(index);
  }

  public int getItemCount() {
    return getInvoiceList().size();
  }

  private List<ArchivedInvoice> getInvoiceList() {
    return this.invoiceList.stream()
        .filter(this::filterByActiveCompany)
        .collect(Collectors.toList());
  }

  private List<ArchivedInvoice> getUnfilteredInvoiceList() {
    return this.invoiceList;
  }

  private boolean filterByActiveCompany(ArchivedInvoice invoice) {
    return filterByCompany(invoice, Session.getActiveCompany());
  }

  private boolean filterByCompany(ArchivedInvoice invoice, Company company) {
    return invoice.getCompany().equals(company);
  }

  private Predicate<? super ArchivedInvoice> filterByCompany(Company company) {
    return invoice -> filterByCompany(invoice, company);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return COLUMNS[columnIndex].getClass();
  }

  @Override
  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return COLUMNS[columnIndex].getName();
  }

  public void determineHighestInvoiceNumbers() {
    getUnfilteredInvoiceList()
        .stream()
        .map(ArchivedInvoice::getCompany)
        .distinct()
        .forEach(c -> {
              c.setInvoiceNumber(getUnfilteredInvoiceList()
                  .stream()
                  .filter(filterByCompany(c))
                  .mapToInt(ArchivedInvoice::getInvoiceNumber)
                  .max()
                  .orElse(0)
              );

              Session.notifyCompany();
            }
        );
  }

  public File getPdfAt(int rowIndex) {
    final ArchivedInvoice data = this.get(rowIndex);
    return data.getPdf();
  }

  @Override
  public int getRowCount() {
    return getItemCount();
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
