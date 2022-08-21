package de.heinerion.invoice.view.swing.menu.tablemodels;

import javax.swing.table.TableModel;
import java.util.Optional;

public interface RowSelectionTableModel<T> extends TableModel {
  Optional<T> getRow(int rowIndex);
}
