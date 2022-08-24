package de.heinerion.invoice.view.swing.menu.tablemodels.letters;

import de.heinerion.invoice.models.Letter;
import de.heinerion.invoice.view.swing.menu.tablemodels.RowSelectionTableModel;

import javax.swing.event.TableModelListener;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LetterTable implements RowSelectionTableModel<Letter> {
  private final List<Letter> letters;

  public LetterTable(Collection<Letter> letters) {
    this.letters = letters.stream()
        .sorted(Comparator.comparing(Letter::getDate).thenComparing(Letter::getSubject))
        .toList();
  }

  private LetterColumn getColumn(int columnIndes) {
    return LetterColumn.values()[columnIndes];
  }

  @Override
  public int getRowCount() {
    return letters.size();
  }

  @Override
  public int getColumnCount() {
    return LetterColumn.values().length;
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
    return getColumn(columnIndex).getValue(letters.get(rowIndex));
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    throw new UnsupportedOperationException();
  }

  public Optional<Letter> getRow(int rowIndex) {
    return (rowIndex < 0 || rowIndex > letters.size())
        ? Optional.empty()
        : Optional.of(letters.get(rowIndex));
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
