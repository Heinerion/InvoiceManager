package de.heinerion.betriebe.gui.content;

import de.heinerion.betriebe.classes.file_operations.IO;
import de.heinerion.betriebe.classes.texting.DropListable;
import de.heinerion.betriebe.classes.texting.Vorlage;
import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.listener.ConveyableListener;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Item;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.tools.FormatTools;
import de.heinerion.betriebe.tools.ParsingTools;
import de.heinerion.betriebe.tools.strings.Strings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public final class InvoiceTabContent extends AbstractTabContent implements
    ConveyableListener {
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
  private JLabel currentTotalNet = new JLabel();
  private JLabel currentTotalGross = new JLabel();

  protected InvoiceTabContent() {
    super(Constants.INVOICE);
    Session.addConveyableListener(this);

    final TableModel model = new InvoiceTableModel(contentPositions);
    model.addTableModelListener(e -> Session.setActiveConveyable(getContent()));
    tabPositions = new JTable(model);

    final JButton btnVorlagenSave = new JButton(
        (Icon) UIManager.get("FileChooser.floppyDriveIcon"));
    btnVorlagenSave.addActionListener(e -> saveTemplate());

    final JPanel pnlVorlagen = new JPanel(new BorderLayout());
    pnlVorlagen.setOpaque(false);

    final JLabel lblVorlagen = new JLabel("Vorlagen");
    lblVorlagen.setFont(lblVorlagen.getFont().deriveFont(Font.BOLD));

    pnlVorlagen.add(lblVorlagen, BorderLayout.LINE_START);
    pnlVorlagen.add(templateBox, BorderLayout.CENTER);
    pnlVorlagen.add(btnVorlagenSave, BorderLayout.LINE_END);

    setLayout(new BorderLayout());
    add(pnlVorlagen, BorderLayout.PAGE_START);
    add(new JScrollPane(this.tabPositions), BorderLayout.CENTER);

    final JPanel pnlSum = new JPanel(new GridLayout(1, 0));
    pnlSum.setOpaque(false);
    pnlSum.add(new JLabel("Vor Steuern"));
    pnlSum.add(currentTotalNet);
    pnlSum.add(new JLabel("Nach Steuern"));
    pnlSum.add(currentTotalGross);

    final JPanel pnlFooter = new JPanel(new GridLayout(0, 1));
    pnlFooter.setOpaque(false);
    pnlFooter.add(pnlSum, BorderLayout.PAGE_END);
    pnlFooter.add(getDelete());

    add(pnlFooter, BorderLayout.PAGE_END);

    this.templateBox.addActionListener(e -> updateSelection());
  }

  private void updateSelection() {
    final int pos = this.templateBox.getSelectedIndex();
    if (pos >= 0) {
      // Überschreibe Tabelleninhalt mit Vorlage
      String[][] tabelle;
      final List<Vorlage> activeVorlagen = DataBase.getTemplates(Session
          .getActiveCompany());
      tabelle = activeVorlagen.get(pos).getInhalt();
      fillTable(tabelle);
    }
  }

  private void fillTable(String[][] tabelle) {
    String[] zeile;
    for (int row = 0; row < tabelle.length; row++) {
      zeile = tabelle[row];
      for (int col = 0; col < zeile.length; col++) {
        // Für jede Zeile der Spalte die Zellen kopieren
        final String content = tabelle[row][col];
        final Class<?> colClass = tabPositions.getColumnClass(col);
        if (String.class.equals(colClass)) {
          tabPositions.setValueAt(content, row, col);
        } else if (Double.class.equals(colClass)) {
          tabPositions.setValueAt(ParsingTools.parseDouble(content), row, col);
        }
      }
    }
  }

  @Override
  protected void clear() {
    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        // Alle Zellen überschreiben
        this.tabPositions.setValueAt("", i, j);
      }
    }
  }

  private void saveTemplate() {
    final Item item = contentPositions.get(0);
    if (item != null && item.getName() != null && !"".equals(item.getName())) {
      final List<Vorlage> activeVorlagen = DataBase.getTemplates(Session
          .getActiveCompany());
      // Erste Zelle ist Name
      final String name = item.getName();
      // Wenn bereits Daten vorhanden: Index, sonst -1
      final int index = this.index(name, activeVorlagen);
      final Vorlage vorlage = createVorlage();
      if (index == -1) {
        // Daten erstellen
        activeVorlagen.add(vorlage);
      } else {
        // Daten aktualisieren
        activeVorlagen.set(index, vorlage);
      }
      Collections.sort(activeVorlagen);
      IO.speichereVorlagen(activeVorlagen, Session.getActiveCompany());
      // TODO hässliche, langsame Lösung, erfordert repaint
      IO.ladeVorlagen();
      refresh();
    }
  }

  private Vorlage createVorlage() {
    Vorlage result = null;
    if (tabPositions.getModel() instanceof InvoiceTableModel) {
      final TableModel tableModel = tabPositions.getModel();
      final InvoiceTableModel model = (InvoiceTableModel) tableModel;
      result = model.createVorlage();
    }
    return result;
  }

  @Override
  public void refresh() {
    final List<Vorlage> activeVorlagen = DataBase.getTemplates(Session
        .getActiveCompany());
    if (!activeVorlagen.equals(templates)) {
      this.templateBox.removeAllItems();
      for (final Vorlage vorlage : activeVorlagen) {
        this.templateBox.addItem(vorlage);
      }
      templates = activeVorlagen;
    }
    Session.setActiveConveyable(getContent());
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
    for (final T vorlage : list) {
      if (vorlage.getName().equals(name)) {
        if (logger.isDebugEnabled()) {
          logger.debug("{} = {}", vorlage.getName(), name);
        }
        return list.indexOf(vorlage);
      }
    }
    // Wenn die Methode bis hier kommt, gibt es keine Übereinstimmung
    return -1;
  }

  @Override
  protected Conveyable getConveyable() {
    final Company company = Session.getActiveCompany();
    final Address receiver = Session.getActiveAddress();

    final Invoice invoice = new Invoice(Session.getDate(), company, receiver);

    // TODO warum +1?
    for (int row = 0; row < Constants.INVOICE_LINECOUNT + 1; row++) {
      final String name = stringAt(row, COL_NAME);
      final String unit = stringAt(row, COL_UNIT);
      final Double price = doubleAt(row, COL_PRICE);
      final Double count = doubleAt(row, COL_COUNT);

      if (Strings.isEmpty(unit)) {
        if (!Strings.isEmpty(name)) {
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
    final Object value = tabPositions.getValueAt(row, col);
    if (value instanceof String) {
      result = (String) value;
    }
    return result;
  }

  private Double doubleAt(int row, int col) {
    Double result = null;
    final Object value = tabPositions.getValueAt(row, col);
    if (value instanceof Double) {
      result = (Double) value;
    }
    return result;
  }

  @Override
  public void notifyConveyable() {
    if (Session.getActiveConveyable() instanceof Invoice) {
      final Invoice invoice = (Invoice) Session.getActiveConveyable();
      currentTotalGross.setText(FormatTools.formatLocaleDecimal(invoice
          .getGross()));
      currentTotalNet
          .setText(FormatTools.formatLocaleDecimal(invoice.getNet()));
    }
  }
}
