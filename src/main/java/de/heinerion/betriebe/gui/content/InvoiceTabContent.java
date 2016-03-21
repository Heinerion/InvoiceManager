package de.heinerion.betriebe.gui.content;

import de.heinerion.betriebe.fileoperations.IO;
import de.heinerion.betriebe.classes.texting.DropListable;
import de.heinerion.betriebe.classes.texting.Vorlage;
import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.exceptions.HeinerionException;
import de.heinerion.betriebe.gui.content.components.FooterPanel;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Item;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.tools.ParsingUtil;
import de.heinerion.betriebe.tools.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.awt.BorderLayout.*;

@SuppressWarnings("serial")
public final class InvoiceTabContent extends AbstractTabContent {
  // TODO aus TableModel beziehen
  private static final int COL_NAME = 0;
  private static final int COL_UNIT = 1;
  private static final int COL_PRICE = 2;
  private static final int COL_COUNT = 3;

  private static final Logger logger = LogManager.getLogger(InvoiceTabContent.class);

  // TODO Reihen und Spalten dynamisch
  private final static int ROWS = 7;
  private final static int COLS = 4;

  private final List<Item> contentPositions = new ArrayList<>();
  private List<Vorlage> templates = new ArrayList<>();

  private JTable tabPositions;
  private final JComboBox<Vorlage> templateBox = new JComboBox<>();

  protected InvoiceTabContent() {
    super(Translator.translate("invoice.title"));
    initTabPositions();

    setLayout(new BorderLayout());

    add(createTemplatePnl(), PAGE_START);
    add(createTablePnl(), CENTER);
    add(createFooterPnl(), PAGE_END);

    setUpInteractions();
  }

  private void initTabPositions() {
    TableModel model = new InvoiceTableModel(contentPositions);
    model.addTableModelListener(e -> Session.setActiveConveyable(getContent()));
    tabPositions = new JTable(model);
  }

  private JPanel createTemplatePnl() {
    JPanel templatePnl = new JPanel(new BorderLayout());

    templatePnl.setOpaque(false);
    templatePnl.add(createTemplateLbl(), LINE_START);
    templatePnl.add(templateBox, CENTER);
    templatePnl.add(createTemplateSaveBtn(), LINE_END);

    return templatePnl;
  }

  private JLabel createTemplateLbl() {
    JLabel templateLbl = new JLabel("Vorlagen");
    templateLbl.setFont(templateLbl.getFont().deriveFont(Font.BOLD));
    return templateLbl;
  }

  private JButton createTemplateSaveBtn() {
    JButton saveTemplateBtn = new JButton(
        (Icon) UIManager.get("FileChooser.floppyDriveIcon"));

    saveTemplateBtn.addActionListener(e -> saveTemplate());

    return saveTemplateBtn;
  }

  private void saveTemplate() {
    Item item = contentPositions.get(0);
    if (isNotEmpty(item)) {
      addToTemplates(item);
    }
  }

  private boolean isNotEmpty(Item item) {
    return !(item == null || item.getName() == null || "".equals(item.getName().trim()));
  }

  private void addToTemplates(Item item) {
    List<Vorlage> activeTemplates = DataBase.getTemplates(Session.getActiveCompany());
    Vorlage template = createTemplate();

    int index = index(item.getName(), activeTemplates);
    if (index == -1) {
      activeTemplates.add(template);
    } else {
      activeTemplates.set(index, template);
    }

    Collections.sort(activeTemplates);
    IO.updateTemplates(activeTemplates);
    refresh();
  }

  private Vorlage createTemplate() {
    Vorlage result = null;

    if (tabPositions.getModel() instanceof InvoiceTableModel) {
      TableModel tableModel = tabPositions.getModel();
      InvoiceTableModel model = (InvoiceTableModel) tableModel;
      result = model.createVorlage();
    }

    return result;
  }

  @Override
  public void refresh() {
    List<Vorlage> activeTemplates = DataBase.getTemplates(Session.getActiveCompany());
    if (!activeTemplates.equals(templates)) {
      templateBox.removeAllItems();
      for (Vorlage template : activeTemplates) {
        templateBox.addItem(template);
      }
      templates = activeTemplates;
    }
    Session.setActiveConveyable(getContent());
  }

  private JScrollPane createTablePnl() {
    return new JScrollPane(tabPositions);
  }

  private JPanel createFooterPnl() {
    return new FooterPanel(getDeleteBtn());
  }

  private void setUpInteractions() {
    templateBox.addActionListener(e -> updateSelection());
  }

  private void updateSelection() {
    int pos = templateBox.getSelectedIndex();
    if (pos >= 0) {
      // Überschreibe Tabelleninhalt mit Vorlage
      String[][] table;
      List<Vorlage> activeTemplates = DataBase.getTemplates(Session.getActiveCompany());
      table = activeTemplates.get(pos).getInhalt();
      fillTable(table);
    }
  }

  private void fillTable(String[][] tabelle) {
    for (int row = 0; row < tabelle.length; row++) {
      fillRow(tabelle[row], row);
    }
  }

  private void fillRow(String[] rowContents, int row) {
    for (int col = 0; col < rowContents.length; col++) {
      fillCell(row, col, rowContents[col]);
    }
  }

  private void fillCell(int row, int col, String content) {
    Class<?> colClass = tabPositions.getColumnClass(col);
    if (String.class.equals(colClass)) {
      tabPositions.setValueAt(content, row, col);
    } else if (Double.class.equals(colClass)) {
      tabPositions.setValueAt(ParsingUtil.parseDouble(content), row, col);
    } else {
      throw new HeinerionException("Invalid column class " + colClass.getSimpleName());
    }
  }

  @Override
  protected void clear() {
    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        // Alle Zellen überschreiben
        tabPositions.setValueAt("", i, j);
      }
    }
  }

  /**
   * Bestimmt den Index der gesuchten Eintrags in der gegebenen Liste TODO
   * index(String, ArrayList) in DropListeable schieben
   *
   * @param name Der Name des Eintrags
   * @param list Die zu durchsuchende Liste
   * @return Den Index des Eintrags oder -1, wenn nicht vorhanden
   */
  private <T extends DropListable> int index(String name, List<T> list) {
    int index = -1;

    for (T vorlage : list) {
      if (vorlage.getName().equals(name)) {
        if (logger.isDebugEnabled()) {
          logger.debug("{} = {}", vorlage.getName(), name);
        }
        index = list.indexOf(vorlage);
      }
    }

    return index;
  }

  @Override
  protected Conveyable getConveyable() {
    Company company = Session.getActiveCompany();
    Address receiver = Session.getActiveAddress();

    Invoice invoice = new Invoice(Session.getDate(), company, receiver);

    // TODO warum +1?
    for (int row = 0; row < Constants.INVOICE_LINECOUNT + 1; row++) {
      String name = stringAt(row, COL_NAME);
      String unit = stringAt(row, COL_UNIT);
      Double price = doubleAt(row, COL_PRICE);
      Double count = doubleAt(row, COL_COUNT);

      if (StringUtil.isEmpty(unit)) {
        if (!StringUtil.isEmpty(name)) {
          // Außer Bezeichnung alles leer →
          // Multispalte
          invoice.add(name, null, 0, 0);
        }
      } else {
        invoice.add(name, unit, price, count);
      }
    }

    return invoice;
  }

  private String stringAt(int row, int col) {
    String result = null;
    Object value = tabPositions.getValueAt(row, col);
    if (value instanceof String) {
      result = (String) value;
    }
    return result;
  }

  private Double doubleAt(int row, int col) {
    Double result = null;
    Object value = tabPositions.getValueAt(row, col);
    if (value instanceof Double) {
      result = (Double) value;
    }
    return result;
  }
}
