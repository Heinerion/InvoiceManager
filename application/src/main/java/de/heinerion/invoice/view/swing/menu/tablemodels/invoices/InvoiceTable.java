package de.heinerion.invoice.view.swing.menu.tablemodels.invoices;

import de.heinerion.invoice.models.Invoice;
import de.heinerion.invoice.view.swing.menu.tablemodels.RowSelectionTableModel;

import javax.swing.event.TableModelListener;
import java.util.*;

public class InvoiceTable implements RowSelectionTableModel<Invoice> {
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

  public Optional<Invoice> getRow(int rowIndex) {
    return (rowIndex < 0 || rowIndex > invoices.size())
        ? Optional.empty()
        : Optional.of(invoices.get(rowIndex));
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
