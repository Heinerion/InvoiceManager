/**
 * GemeindenMenu.java
 * heiner 30.03.2012
 */
package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.classes.gui.RechnungFrame;
import de.heinerion.betriebe.tools.DimensionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
public abstract class AbstractDropListenMenu extends AbstractMenu {
  private static final Logger LOGGER = LogManager.getLogger(AbstractDropListenMenu.class);

  /**
   * Tabelleninhalt
   */
  private String[][] inhalt;

  /**
   * Tabelle mit Inhalt
   */
  private JTable tabelle;
  /**
   * Scrollrahmen
   */
  private JScrollPane scrollpane;

  public AbstractDropListenMenu(final RechnungFrame origin) {
    super(origin);
    this.setMinimumSize(DimensionUtil.MENU);
  }

  @Override
  protected final void addWidgets() {
    this.setLayout(new BorderLayout());
    final JPanel halter = new JPanel();
    // nebeneinander
    halter.setLayout(new GridLayout(1, 0));
    halter.add(getBtnDelete());
    halter.add(getBtnOk());
    this.add(halter, BorderLayout.PAGE_END);
    this.add(this.scrollpane, BorderLayout.CENTER);
  }

  @Override
  protected final void createWidgets() {
    final String[] titel = this.init();
    this.tabelle = new JTable(new DefaultTableModel(this.inhalt, titel));
    this.scrollpane = new JScrollPane(this.tabelle);
  }

  /**
   * Definiert welche Aktionen beim Drücken des OK Knopfes ausgeführt werden
   * sollen.
   *
   * @return Den Listener der das geforderte leistet
   */
  protected abstract ActionListener getListener();

  /**
   * Legt Tabelleninhalte fest und gibt die Tabellenköpfe zurück
   *
   * @return Die Tabellenköpfe
   */
  protected abstract String[] init();

  @Override
  protected final void setupInteractions() {
    getBtnOk().addActionListener(this.getListener());
    // hm 29.03.2013: Löschbutton beschaltet
    getBtnDelete().addActionListener(e -> deleteAllRows());
  }

  /**
   *
   */
  private void deleteAllRows() {
    for (int i = 0; i < this.tabelle.getRowCount(); i++) {
      if (this.tabelle.isRowSelected(i)) {
        deleteRow(i);
      }
    }
  }

  private void deleteRow(int row) {
    String inhalte = "";
    for (int col = 0; col < this.tabelle.getColumnCount(); col++) {
      inhalte += this.tabelle.getValueAt(row, col) + " ";
    }
    final String meldung = "Gelöscht: " + inhalte;
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(meldung);
    }
    ((DefaultTableModel) this.tabelle.getModel()).removeRow(row);
  }
}
