package de.heinerion.invoice.view.swing.tab;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.template.TemplateRepository;
import de.heinerion.contract.ContractBrokenException;
import de.heinerion.invoice.NumberParser;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.TabContent;
import lombok.extern.flogger.Flogger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.awt.BorderLayout.*;

@Flogger
@Service
@Order(2)
class InvoiceTabContent extends TabContent {
  private final Session session = Session.getInstance();
  private final List<Item> contentPositions = new ArrayList<>();
  private final JTable tabPositions;
  private final JComboBox<InvoiceTemplate> templateBox = new JComboBox<>();
  private final InvoiceTableModel model;
  private final TemplateRepository templateRepository;

  private Collection<InvoiceTemplate> templates = new ArrayList<>();

  InvoiceTabContent(TemplateRepository templateRepository) {
    super(Translator.translate("invoice.title"));
    this.templateRepository = templateRepository;

    model = new InvoiceTableModel(contentPositions);
    model.addTableModelListener(e -> session.setActiveConveyable(getContent()));
    tabPositions = new JTable(model);

    setupPanel(getPanel());
    setUpInteractions();
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
    templatePnl.add(templateBox, CENTER);
    templatePnl.add(createTemplateSaveBtn(), LINE_END);

    return templatePnl;
  }

  private JLabel createTemplateLbl() {
    JLabel templateLbl = new JLabel(Translator.translate("template.plural"));
    templateLbl.setFont(templateLbl.getFont().deriveFont(Font.BOLD));
    return templateLbl;
  }

  private JButton createTemplateSaveBtn() {
    Icon saveIcon = UIManager.getIcon(Translator.translate("icons.save"));
    JButton saveTemplateBtn = new JButton(saveIcon);
    saveTemplateBtn.addActionListener(e -> saveTemplate());

    return saveTemplateBtn;
  }

  private void saveTemplate() {
    if (isNotEmpty(contentPositions.get(0))) {
      addToTemplates();
    }
  }

  private boolean isNotEmpty(Item item) {
    return !(item == null || item.getName() == null || "".equals(item.getName().trim()));
  }

  private void addToTemplates() {
    Company activeCompany = session.getActiveCompany()
        .orElseThrow(() -> new ContractBrokenException("active company is not null"));
    templateRepository.save(createTemplate(activeCompany));
    refresh();
  }

  private InvoiceTemplate createTemplate(Company company) {
    return model != null ? model.createTemplate(company) : null;
  }

  @Override
  public void refresh() {
    Collection<InvoiceTemplate> activeTemplates = session.getActiveCompany()
        .map(templateRepository::findByCompany)
        .orElse(Collections.emptyList());
    if (!activeTemplates.equals(templates)) {
      clearPositions();
      clearTemplates();
      addTemplates(activeTemplates);
      templates = activeTemplates;
    }
    session.setActiveConveyable(getContent());
  }

  private void clearTemplates() {
    templateBox.removeAllItems();
  }

  private void addTemplates(Collection<InvoiceTemplate> templates) {
    templates.forEach(templateBox::addItem);
  }

  private JScrollPane createTablePnl() {
    return new JScrollPane(tabPositions);
  }

  private JPanel createFooterPnl() {
    return new FooterPanel(getDeleteBtn(), session);
  }

  private void setUpInteractions() {
    templateBox.addActionListener(e -> updateSelection());
  }

  private void updateSelection() {
    int pos = templateBox.getSelectedIndex();
    if (pos >= 0) {
      // replace table positions with those of the template
      Collection<InvoiceTemplate> activeTemplates = session.getActiveCompany()
          .map(templateRepository::findByCompany)
          .orElse(Collections.emptyList());
      fillTable(new ArrayList<>(activeTemplates).get(pos).getInhalt());
      model.fireTableDataChanged();
    }
  }

  private void fillTable(String[][] table) {
    for (int row = 0; row < table.length; row++) {
      fillRow(table[row], row);
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
      tabPositions.setValueAt(NumberParser.parseDouble(content), row, col);
    } else {
      throw new GuiPanelException("Invalid column class " + colClass.getSimpleName());
    }
  }

  @Override
  protected void clear() {
    clearPositions();
  }

  private void clearPositions() {
    for (int i = 0; i < contentPositions.size(); i++) {
      for (int j = 0; j < tabPositions.getColumnCount(); j++) {
        // overwrite all rows
        tabPositions.setValueAt("", i, j);
      }
    }
  }

  @Override
  protected Letter getConveyable() {
    Company company = session.getActiveCompany().orElse(null);
    if (company == null) {
      return null;
    }

    Address receiver = session.getActiveAddress();
    Invoice invoice = new Invoice(session.getDate(), company, receiver);

    for (int row = 0; row < tabPositions.getRowCount(); row++) {
      String name = stringAt(row, Col.NAME);
      String unit = stringAt(row, Col.UNIT);
      Double price = doubleAt(row, Col.PRICE);
      Double count = doubleAt(row, Col.COUNT);

      if (isEmpty(unit)) {
        if (!isEmpty(name)) {
          // Nothing but a description? → multi column
          invoice.addMessageLine(name);
        }
      } else {
        invoice.add(name, unit, price, count);
      }
    }

    return invoice;
  }

  private boolean isEmpty(String givenString) {
    return givenString == null || givenString.trim().isEmpty();
  }

  private String stringAt(int row, Col col) {
    String result = null;
    Object value = getPositionAt(row, col);
    if (value instanceof String stringValue) {
      result = stringValue;
    }
    return result;
  }

  private Double doubleAt(int row, Col col) {
    Double result = null;
    Object value = getPositionAt(row, col);
    if (value instanceof Double doubleValue) {
      result = doubleValue;
    }
    return result;
  }

  private Object getPositionAt(int row, Col col) {
    return tabPositions.getValueAt(row, col.pos);
  }

  private enum Col {
    NAME(0), UNIT(1), PRICE(2), COUNT(3);

    final int pos;

    Col(int pos) {
      this.pos = pos;
    }
  }
}
