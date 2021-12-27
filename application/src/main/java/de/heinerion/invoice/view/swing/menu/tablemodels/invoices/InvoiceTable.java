package de.heinerion.invoice.view.swing.menu.tablemodels.invoices;

import de.heinerion.betriebe.models.Invoice;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class InvoiceTable implements TableModel {
  private final List<Invoice> invoices;

  public InvoiceTable(Collection<Invoice> invoices) {
    this.invoices = invoices.stream()
        .sorted(Comparator.comparing(Invoice::getCompany).thenComparing(Invoice::getNumber))
        .toList();
  }

  private InvoiceColumn getColumn(int columnIndes) {
    return InvoiceColumn.values()[columnIndes];
  }

  public double calculateRevenues() {
    return invoices.stream()
        .mapToDouble(Invoice::getGross)
        .sum();
  }

  @Override
  public int getRowCount() {
    return invoices.size();
  }

  @Override
  public int getColumnCount() {
    return InvoiceColumn.values().length;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return getColumn(columnIndex).getName();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return getColumn(columnIndex).getColumnClass();
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return getColumn(columnIndex).isEditable();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return getColumn(columnIndex).getValue(invoices.get(rowIndex));
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addTableModelListener(TableModelListener l) {
    // no updates -> no listeners
  }

  @Override
  public void removeTableModelListener(TableModelListener l) {
    // no updates -> no listeners
  }
}
