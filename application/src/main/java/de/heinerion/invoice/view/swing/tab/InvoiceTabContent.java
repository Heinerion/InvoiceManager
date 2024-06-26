package de.heinerion.invoice.view.swing.tab;

import de.heinerion.contract.ContractBrokenException;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.*;
import de.heinerion.invoice.view.swing.TabContent;
import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;
import de.heinerion.util.Strings;
import lombok.extern.flogger.Flogger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.text.Collator;
import java.util.List;
import java.util.*;

import static java.awt.BorderLayout.*;

@Flogger
@Service
@Order(2)
class InvoiceTabContent extends TabContent {
  private final Session session;
  private final List<Item> contentPositions = new ArrayList<>();
  private final JTable tabPositions;
  private final JComboBox<InvoiceTemplate> templateBox = new JComboBox<>();
  private final InvoiceTableModel model;
  private final InvoiceTemplateRepository templateRepository;
  private final TemplateItemRepository templateItemRepository;

  private Collection<InvoiceTemplate> templates = new ArrayList<>();

  InvoiceTabContent(InvoiceTemplateRepository templateRepository, ProductRepository productRepository, TemplateItemRepository templateItemRepository, Session session, LookAndFeelUtil lookAndFeelUtil) {
    super(lookAndFeelUtil, Translator.translate("invoice.title"));
    this.templateRepository = templateRepository;
    this.templateItemRepository = templateItemRepository;
    this.session = session;

    model = new InvoiceTableModel(contentPositions, templateRepository, productRepository);
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
    return !(item == null || Strings.isBlank(item.getName()));
  }

  private void addToTemplates() {
    Company activeCompany = session.getActiveCompany()
        .orElseThrow(() -> new ContractBrokenException("active company is not null"));
    createTemplate(activeCompany).ifPresent(this::persist);
    refresh();
  }

  private void persist(InvoiceTemplate template) {
    InvoiceTemplate t = templateRepository.save(template);
    templateItemRepository.saveAll(template.getTemplateItems().stream()
        .map(i -> i.setTemplate(t))
        .toList());
  }

  private Optional<InvoiceTemplate> createTemplate(Company company) {
    return Optional.ofNullable(model)
        .map(m -> m.createTemplate(company));
  }

  @Override
  public void refresh() {
    Collection<InvoiceTemplate> activeTemplates = session.getActiveCompany()
        .map(templateRepository::findByCompany)
        .orElse(Collections.emptyList());
    if (!activeTemplates.equals(templates)) {
      clear();
      clearTemplates();
      addTemplates(activeTemplates);
      templates = activeTemplates;
      if (!templates.isEmpty()) {
        templateBox.setSelectedIndex(0);
      }
    }
    session.setActiveConveyable(getContent());
  }

  private void clearTemplates() {
    templateBox.removeAllItems();
  }

  private void addTemplates(Collection<InvoiceTemplate> templates) {
    templates.stream()
        .sorted(Comparator.comparing(InvoiceTemplate::getName, Collator.getInstance()::compare))
        .forEach(templateBox::addItem);
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
      List<InvoiceTemplate> activeTemplates = session.getActiveCompany()
          .map(templateRepository::findByCompany)
          .orElse(Collections.emptyList())
          .stream()
          .sorted(Comparator.comparing(
              InvoiceTemplate::getName,
              Collator.getInstance()::compare))
          .toList();

      fillTable(activeTemplates.get(pos).getItems());
      model.fireTableDataChanged();
    }
  }

  private void fillTable(Collection<Item> items) {
    clear();
    items.stream()
        .sorted(Comparator.comparing(Item::getPosition))
        .forEach(model::addRow);
  }

  @Override
  protected void clear() {
    model.clear();
  }

  @Override
  protected Conveyable getConveyable() {
    Company company = session.getActiveCompany().orElse(null);
    if (company == null) {
      return null;
    }

    Address receiver = session.getActiveAddress();
    Invoice invoice = new Invoice(session.getDate(), company, receiver, company.getInvoiceNumber());

    for (Item item : model.getItems()) {
      invoice.addItem(item);
    }

    return invoice;
  }
}
