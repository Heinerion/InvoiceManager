package de.heinerion.betriebe.view.panels.tab;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.data.listable.DropListable;
import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.loading.IO;
import de.heinerion.betriebe.models.*;
import de.heinerion.util.ParsingUtil;
import de.heinerion.util.StringUtil;
import de.heinerion.util.Translator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.awt.BorderLayout.*;

@SuppressWarnings("serial")
class InvoiceTabContent extends AbstractTabContent {
  private static final Logger logger = LogManager.getLogger(InvoiceTabContent.class);
  // TODO rows and cols dynamic
  private static final int ROWS = 7;
  private static final int COLS = 4;
  private List<Item> contentPositions;
  private List<InvoiceTemplate> templates;
  private JTable tabPositions;
  private JComboBox<InvoiceTemplate> templateBox;
  private IO io;

  @Autowired
  InvoiceTabContent(IO io) {
    super(Translator.translate("invoice.title"));

    this.io = io;

    initTabPositions();

    setupPanel(getPanel());
    setUpInteractions();
  }

  private void initTabPositions() {
    TableModel model = new InvoiceTableModel(getContentPositions());
    model.addTableModelListener(e -> Session.setActiveConveyable(getContent()));
    setTabPositions(new JTable(model));
  }

  private void setupPanel(JPanel panel) {
    panel.setLayout(new BorderLayout());

    panel.add(createTemplatePnl(), PAGE_START);
    panel.add(createTablePnl(), CENTER);
    panel.add(createFooterPnl(), PAGE_END);
  }

  private JPanel createTemplatePnl() {
    JPanel templatePnl = new JPanel(new BorderLayout());

    templatePnl.setOpaque(false);
    templatePnl.add(createTemplateLbl(), LINE_START);
    templatePnl.add(getTemplateBox(), CENTER);
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
    Item item = getContentPositions().get(0);
    if (isNotEmpty(item)) {
      addToTemplates(item);
    }
  }

  private boolean isNotEmpty(Item item) {
    return !(item == null || item.getName() == null || "".equals(item.getName().trim()));
  }

  private void addToTemplates(Item item) {
    List<InvoiceTemplate> activeTemplates = DataBase.getTemplates(Session.getActiveCompany());
    InvoiceTemplate template = createTemplate();

    int index = index(item.getName(), activeTemplates);
    if (index == -1) {
      activeTemplates.add(template);
    } else {
      activeTemplates.set(index, template);
    }

    Collections.sort(activeTemplates);
    io.updateTemplates(activeTemplates);
    refresh();
  }

  private InvoiceTemplate createTemplate() {
    InvoiceTemplate result = null;

    TableModel tableModel = getTableModel();
    if (tableModel instanceof InvoiceTableModel) {
      InvoiceTableModel model = (InvoiceTableModel) tableModel;
      result = model.createVorlage();
    }

    return result;
  }

  private TableModel getTableModel() {
    return getTabPositions().getModel();
  }

  @Override
  public void refresh() {
    List<InvoiceTemplate> activeTemplates = DataBase.getTemplates(Session.getActiveCompany());
    if (!activeTemplates.equals(getTemplates())) {
      clearTemplates();
      addTemplates(activeTemplates);
      setTemplates(activeTemplates);
    }
    Session.setActiveConveyable(getContent());
  }

  private void clearTemplates() {
    getTemplateBox().removeAllItems();
  }

  private void addTemplates(List<InvoiceTemplate> templates) {
    templates.forEach(getTemplateBox()::addItem);
  }

  private JScrollPane createTablePnl() {
    return new JScrollPane(getTabPositions());
  }

  private JPanel createFooterPnl() {
    return new FooterPanel(getDeleteBtn());
  }

  private void setUpInteractions() {
    addTemplateBoxListener(e -> updateSelection());
  }

  private void addTemplateBoxListener(ActionListener actionListener) {
    getTemplateBox().addActionListener(actionListener);
  }

  private void updateSelection() {
    int pos = getTemplateBox().getSelectedIndex();
    if (pos >= 0) {
      // replace table positions with those of the template
      String[][] table;
      List<InvoiceTemplate> activeTemplates = DataBase.getTemplates(Session.getActiveCompany());
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
    JTable positions = getTabPositions();
    Class<?> colClass = positions.getColumnClass(col);
    if (String.class.equals(colClass)) {
      positions.setValueAt(content, row, col);
    } else if (Double.class.equals(colClass)) {
      positions.setValueAt(ParsingUtil.parseDouble(content), row, col);
    } else {
      throw new GuiPanelException("Invalid column class " + colClass.getSimpleName());
    }
  }

  @Override
  protected void clear() {
    clearPositions();
  }

  private void clearPositions() {
    for (int i = 0; i < getRows(); i++) {
      for (int j = 0; j < getCols(); j++) {
        // overwrite all rows
        getTabPositions().setValueAt("", i, j);
      }
    }
  }

  /**
   * Determines the entries' index in the given list
   *
   * @param name name of the entry
   * @param list the list to search in
   * @return the index of the entry or -1 if not found
   */
  private <T extends DropListable> int index(String name, List<T> list) {
    int index = -1;

    for (T template : list) {
      if (template.getName().equals(name)) {
        if (logger.isDebugEnabled()) {
          logger.debug("{} = {}", template.getName(), name);
        }
        index = list.indexOf(template);
      }
    }

    return index;
  }

  @Override
  protected Letter getConveyable() {
    Company company = Session.getActiveCompany();
    Address receiver = Session.getActiveAddress();

    Invoice invoice = new Invoice(Session.getDate(), company, receiver);

    // TODO why +1?
    for (int row = 0; row < Invoice.getDefaultLineCount() + 1; row++) {
      String name = stringAt(row, Col.NAME);
      String unit = stringAt(row, Col.UNIT);
      Double price = doubleAt(row, Col.PRICE);
      Double count = doubleAt(row, Col.COUNT);

      if (StringUtil.isEmpty(unit)) {
        if (!StringUtil.isEmpty(name)) {
          // Nothing but a description? → multi column
          invoice.add(name, null, 0, 0);
        }
      } else {
        invoice.add(name, unit, price, count);
      }
    }

    return invoice;
  }

  private String stringAt(int row, Col col) {
    String result = null;
    Object value = getPositionAt(row, col);
    if (value instanceof String) {
      result = (String) value;
    }
    return result;
  }

  private Double doubleAt(int row, Col col) {
    Double result = null;
    Object value = getPositionAt(row, col);
    if (value instanceof Double) {
      result = (Double) value;
    }
    return result;
  }

  private Object getPositionAt(int row, Col col) {
    return getTabPositions().getValueAt(row, col.pos);
  }

  private int getRows() {
    return ROWS;
  }

  private int getCols() {
    return COLS;
  }

  private List<Item> getContentPositions() {
    if (contentPositions == null) {
      contentPositions = new ArrayList<>();
    }

    return contentPositions;
  }

  private List<InvoiceTemplate> getTemplates() {
    if (templates == null) {
      setTemplates(new ArrayList<>());
    }

    return templates;
  }

  private void setTemplates(List<InvoiceTemplate> templates) {
    this.templates = templates;
  }

  private JTable getTabPositions() {
    return tabPositions;
  }

  private void setTabPositions(JTable tabPositions) {
    this.tabPositions = tabPositions;
  }

  private JComboBox<InvoiceTemplate> getTemplateBox() {
    if (templateBox == null) {
      templateBox = new JComboBox<>();
    }

    return templateBox;
  }

  public IO getIo() {
    return io;
  }

  public void setIo(IO io) {
    this.io = io;
  }

  private enum Col {
    NAME(0), UNIT(1), PRICE(2), COUNT(3);

    protected final int pos;

    Col(int pos) {
      this.pos = pos;
    }
  }
}
