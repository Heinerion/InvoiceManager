/**
 * GemeindenMenu.java
 * heiner 30.03.2012
 */
package de.heinerion.betriebe.gui.menu;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import de.heinerion.betriebe.classes.data.RechnungsListe;
import de.heinerion.betriebe.classes.gui.RechnungFrame;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.tools.FormatTools;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
public final class VerwaltungMenu extends AbstractMenu {
  private RechnungsListe model;

  private JTable tblDokumente;
  private JScrollPane spDokumente;

  /**  */
  public VerwaltungMenu(final RechnungFrame origin) {
    super(origin);
  }

  @Override
  protected void addWidgets() {
    this.setLayout(new BorderLayout());
    this.add(getBtnOk(), BorderLayout.PAGE_END);
    this.add(this.spDokumente, BorderLayout.CENTER);
  }

  @Override
  protected void createWidgets() {
    this.model = DataBase.getRechnungen();
    this.tblDokumente = new JTable(this.model);
    this.tblDokumente.setAutoCreateRowSorter(true);
    this.tblDokumente.setRowSelectionAllowed(true);
    // Breite der Felder
    final TableColumnModel cols = this.tblDokumente.getColumnModel();
    cols.getColumn(RechnungsListe.INDEX_NUMBER).setMaxWidth(50);
    cols.getColumn(RechnungsListe.INDEX_RECEIVER).setPreferredWidth(150);
    cols.getColumn(RechnungsListe.INDEX_PRODUCT).setPreferredWidth(150);
    cols.getColumn(RechnungsListe.INDEX_DATE).setMinWidth(90);
    cols.getColumn(RechnungsListe.INDEX_DATE).setMaxWidth(90);
    cols.getColumn(RechnungsListe.INDEX_SENDER).setPreferredWidth(100);
    cols.getColumn(RechnungsListe.INDEX_AMOUNT).setMinWidth(80);
    cols.getColumn(RechnungsListe.INDEX_AMOUNT).setMaxWidth(80);
    // Nach Nummer sortieren
    this.tblDokumente.getRowSorter().toggleSortOrder(
        RechnungsListe.INDEX_NUMBER);
    // Mit Höchster anfangen
    this.tblDokumente.getRowSorter().toggleSortOrder(
        RechnungsListe.INDEX_NUMBER);

    this.spDokumente = new JScrollPane(this.tblDokumente);

    this.spDokumente.setPreferredSize(getUrsprung().getSize());
  }

  @Override
  protected void setTitle() {
    this.setTitle("Verwaltung - Gesamtsumme: "
        + FormatTools.formatLocaleDecimal(this.model.berechneEinnahmen())
        + " €");
  }

  @Override
  protected void setupInteractions() {
    this.tblDokumente.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        handleDocumentClick(e);
      }
    });
    getBtnOk().addActionListener(arg0 -> getCloser().windowClosing(null));
  }

  /**
   * @param e
   */
  private void handleDocumentClick(MouseEvent e) {
    if (e.getClickCount() == 2) {
      final int selectedRow = tblDokumente.getSelectedRow();
      final int rowIndex = tblDokumente.convertRowIndexToModel(selectedRow);
      final File pdf = model.getPdfAt(rowIndex);
      try {
        Desktop.getDesktop().open(pdf);
      } catch (final IOException e1) {
        e1.printStackTrace();
      }
    }
  }
}
