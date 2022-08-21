package de.heinerion.invoice.view.swing.menu.tablemodels;

import lombok.extern.flogger.Flogger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Flogger
public class NiceTable<T> {
  private final JTable table;
  private final RowSelectionTableModel<T> model;

  public NiceTable(RowSelectionTableModel<T> model) {
    this.model = model;
    this.table = new JTable(model);
    table.setAutoCreateRowSorter(true);
    table.setRowSelectionAllowed(true);

    ListSelectionModel listModel = table.getSelectionModel();
    listModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  public Optional<T> getSelectedRowObject() {
    return model.getRow(table.convertRowIndexToModel(table.getSelectedRow()));
  }

  public void sortBy(int columnIndex, SortOrder order) {
    TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
    table.setRowSorter(sorter);
    List<RowSorter.SortKey> sortKeys = new ArrayList<>();
    sortKeys.add(new RowSorter.SortKey(columnIndex, order));
    sorter.setSortKeys(sortKeys);
    sorter.sort();
  }

  public TableColumnModel getColumnModel() {
    return table.getColumnModel();
  }

  public JTable asJTable() {
    return table;
  }

  public void addSelectionListener(Consumer<ListSelectionModel> action) {
    table.getSelectionModel()
        .addListSelectionListener((ListSelectionEvent e) -> {
          action.accept((ListSelectionModel) e.getSource());
        });
  }
}
