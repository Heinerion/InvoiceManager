/**
 * GemeindenMenu.java
 * heiner 30.03.2012
 */
package de.heinerion.betriebe.gui.menu;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import de.heinerion.betriebe.classes.gui.BGPanel;
import de.heinerion.betriebe.classes.gui.RechnungFrame;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.tools.DateTools;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
public final class RechnungsdatumMenu extends AbstractMenu {
  private JSpinner fldYY, fldMM, fldDD;
  private JLabel lblYY, lblMM, lblDD;
  private JButton btnHeute;
  private LocalDate date;

  private JLabel header;

  public RechnungsdatumMenu(final RechnungFrame origin) {
    super(origin);
  }

  @Override
  protected void addWidgets() {

    this.setLayout(new BorderLayout(5, 5));
    this.add(this.header, BorderLayout.PAGE_START);
    this.add(new JPanel(new GridLayout(2, 1)) {
      {
        this.add(RechnungsdatumMenu.this.btnHeute);
        this.add(RechnungsdatumMenu.this.getBtnOk());
      }
    }, BorderLayout.PAGE_END);
    this.add(this.createDatePanel(), BorderLayout.CENTER);
  }

  private JPanel createDatePanel() {
    final BGPanel pnlDatum = new BGPanel(BGPanel.LINKS, BGPanel.RECHTS,
        BGPanel.OBEN, BGPanel.UNTEN);
    // Reihen 2, Spalten 3
    pnlDatum.setLayout(new GridLayout(2, 3));

    pnlDatum.add(this.lblDD);
    pnlDatum.add(this.lblMM);
    pnlDatum.add(this.lblYY);
    pnlDatum.add(this.fldDD);
    pnlDatum.add(this.fldMM);
    pnlDatum.add(this.fldYY);

    return pnlDatum;
  }

  @Override
  protected void createWidgets() {
    this.btnHeute = new JButton("Heute");
    this.lblYY = new JLabel("Jahr");
    this.lblMM = new JLabel("Monat");
    this.lblDD = new JLabel("Tag");

    final int[] aktuell = getUrsprung().getRechnungsdatumAsArray();
    this.fldDD = new JSpinner(new SpinnerNumberModel(aktuell[0], 1, 31, 1));
    this.fldMM = new JSpinner(new SpinnerNumberModel(aktuell[1], 1, 12, 1));
    this.fldYY = new JSpinner(new SpinnerNumberModel(aktuell[2], 1990, 2050, 1));
    this.fldYY.setEditor(new JSpinner.NumberEditor(this.fldYY, "#"));

    this.header = new JLabel("Aktuelles Rechnungsdatum", SwingConstants.CENTER);

  }

  private boolean eingabenKorrekt() {
    try {
      this.date = DateTools.parse(this.fldDD.getValue() + "."
          + this.fldMM.getValue() + "." + this.fldYY.getValue());
    } catch (final DateTimeParseException e) {
      return false;
    }
    return true;
  }

  @Override
  protected void setTitle() {
    this.setTitle("Rechnungsdatum");
  }

  @Override
  protected void setupInteractions() {
    getBtnOk().addActionListener(
        arg0 -> {
          if (this.eingabenKorrekt()) {
            Session.setDate(this.date);
            getCloser().windowClosing(null);
          } else {
            JOptionPane.showMessageDialog(this.rootPane,
                "Das eingegebene Datum ist falsch", "Fehler",
                JOptionPane.INFORMATION_MESSAGE);
          }
        });

    this.btnHeute.addActionListener(e -> {
      final LocalDate now = LocalDate.now();
      this.fldDD.setValue(now.getDayOfMonth());
      this.fldMM.setValue(now.getMonthValue());
      this.fldYY.setValue(now.getYear());
    });
  }
}
