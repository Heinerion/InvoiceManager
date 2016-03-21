/**
 * GemeindenMenu.java
 * heiner 30.03.2012
 */
package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.data.RechnungsListe;
import de.heinerion.betriebe.classes.gui.ApplicationFrame;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.exceptions.HeinerionException;
import de.heinerion.betriebe.tools.FormatUtil;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
public final class VerwaltungMenu extends AbstractMenu {
  private RechnungsListe model;

  private JTable tblDokumente;
  private JScrollPane spDokumente;

  /**  */
  public VerwaltungMenu(final ApplicationFrame origin) {
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
    this.model = DataBase.getInvoices();
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
        + FormatUtil.formatLocaleDecimal(this.model.berechneEinnahmen())
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

  private void handleDocumentClick(MouseEvent event) {
    if (event.getClickCount() == 2) {
      final int selectedRow = tblDokumente.getSelectedRow();
      final int rowIndex = tblDokumente.convertRowIndexToModel(selectedRow);
      final File pdf = model.getPdfAt(rowIndex);
      try {
        Desktop.getDesktop().open(pdf);
      } catch (final IOException e) {
        HeinerionException.handleException(getClass(), e);
      }
    }
  }
}
