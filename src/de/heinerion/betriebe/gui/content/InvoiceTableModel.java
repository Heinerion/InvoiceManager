package de.heinerion.betriebe.gui.content;

import de.heinerion.betriebe.classes.texting.Vorlage;
import de.heinerion.betriebe.models.Item;
import de.heinerion.betriebe.models.Product;
import de.heinerion.betriebe.tools.ParsingTools;

import javax.swing.table.AbstractTableModel;
import java.util.List;

@SuppressWarnings("serial")
public class InvoiceTableModel extends AbstractTableModel {

  private static final int NAME = 0;
  private static final int UNIT = 1;
  private static final int PPU = 2;
  private static final int COUNT = 3;

  private List<Item> contents;

  public InvoiceTableModel(List<Item> items) {
    contents = items;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return true;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    Class<?> result = String.class;

    if (columnIndex == COUNT || columnIndex == PPU) {
      result = Double.class;
    }

    return result;
  }

  @Override
  public String getColumnName(int columnIndex) {
    String result;

    switch (columnIndex) {
      case NAME:
        result = "Artikel";
        break;
      case UNIT:
        result = "Einheit";
        break;
      case PPU:
        result = "Einzelpreis";
        break;
      case COUNT:
        result = "Anzahl";
        break;
      default:
        result = columnIndex + "";
    }

    return result;
  }

  @Override
  public int getRowCount() {
    // TODO contents.size()?
    return 7;
  }

  @Override
  public int getColumnCount() {
    // TODO magic number
    return 4;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Object value = null;

    if (isValidIndex(rowIndex)) {
      value = getPropertyValue(rowIndex, columnIndex);
    }

    return value;
  }

  private Object getPropertyValue(int rowIndex, int columnIndex) {
    Object result;
    Item item = contents.get(rowIndex);

    switch (columnIndex) {
      case NAME:
        result = item.getName();
        break;
      case UNIT:
        result = item.getUnit();
        break;
      case PPU:
        result = item.getPricePerUnit();
        break;
      case COUNT:
        result = item.getQuantity();
        break;
      default:
        result = null;
        break;
    }

    return result;
  }

  private boolean isValidIndex(int rowIndex) {
    return rowIndex < contents.size();
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    Item item = getItem(rowIndex);

    switch (columnIndex) {
      case NAME:
        item.setName((String) aValue);
        break;
      case UNIT:
        item.setUnit((String) aValue);
        break;
      case PPU:
        item.setPricePerUnit(parseDouble(aValue));
        break;
      case COUNT:
        item.setQuantity(parseDouble(aValue));
        break;
      default:
        break;
    }

    fireTableCellUpdated(rowIndex, columnIndex);
  }

  private Item getItem(int rowIndex) {
    Item item;
    if (isValidIndex(rowIndex)) {
      item = contents.get(rowIndex);
    } else {
      item = new Item(new Product(null, null, 0));
      contents.add(item);
    }
    return item;
  }

  private double parseDouble(Object aValue) {
    double result = 0;
    if (aValue instanceof String) {
      result = ParsingTools.parseDouble((String) aValue);
    } else if (aValue instanceof Double) {
      result = (Double) aValue;
    }
    return result;
  }

  public Vorlage createVorlage() {
    Vorlage result = new Vorlage();

    result.setName(contents.get(0).getName());
    String[][] contentTable = new String[contents.size()][getColumnCount()];
    for (int i = 0; i < contents.size(); i++) {
      Item item = contents.get(i);
      contentTable[i][NAME] = item.getName();
      contentTable[i][UNIT] = item.getUnit();
      contentTable[i][PPU] = "" + item.getPricePerUnit();
      contentTable[i][COUNT] = "" + item.getQuantity();
    }
    result.setInhalt(contentTable);

    return result;
  }
}