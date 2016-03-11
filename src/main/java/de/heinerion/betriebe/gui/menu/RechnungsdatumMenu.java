/**
 * GemeindenMenu.java
 * heiner 30.03.2012
 */
package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.classes.gui.BGPanel;
import de.heinerion.betriebe.classes.gui.ApplicationFrame;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.tools.DateUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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

  public RechnungsdatumMenu(final ApplicationFrame origin) {
    super(origin);
  }

  @Override
  protected void addWidgets() {
    setLayout(new BorderLayout(5, 5));
    add(header, BorderLayout.PAGE_START);
    add(createBtnPnl(), BorderLayout.PAGE_END);
    add(createDatePanel(), BorderLayout.CENTER);
  }

  private JPanel createBtnPnl() {
    JPanel btnPnl = new JPanel(new GridLayout(2, 1));

    btnPnl.add(btnHeute);
    btnPnl.add(getBtnOk());

    return btnPnl;
  }

  private JPanel createDatePanel() {
    final BGPanel pnlDatum = new BGPanel(BGPanel.LEFT, BGPanel.RIGHT,
        BGPanel.TOP, BGPanel.BOTTOM);
    // Reihen 2, Spalten 3
    pnlDatum.setLayout(new GridLayout(2, 3));

    pnlDatum.add(lblDD);
    pnlDatum.add(lblMM);
    pnlDatum.add(lblYY);
    pnlDatum.add(fldDD);
    pnlDatum.add(fldMM);
    pnlDatum.add(fldYY);

    return pnlDatum;
  }

  @Override
  protected void createWidgets() {
    btnHeute = new JButton("Heute");
    lblYY = new JLabel("Jahr");
    lblMM = new JLabel("Monat");
    lblDD = new JLabel("Tag");

    final int[] aktuell = getUrsprung().getRechnungsdatumAsArray();
    fldDD = new JSpinner(new SpinnerNumberModel(aktuell[0], 1, 31, 1));
    fldMM = new JSpinner(new SpinnerNumberModel(aktuell[1], 1, 12, 1));
    fldYY = new JSpinner(new SpinnerNumberModel(aktuell[2], 1990, 2050, 1));
    fldYY.setEditor(new JSpinner.NumberEditor(fldYY, "#"));

    header = new JLabel("Aktuelles Rechnungsdatum", SwingConstants.CENTER);

  }

  private boolean eingabenKorrekt() {
    boolean correct = true;

    try {
      date = DateUtil.parse(fldDD.getValue() + "." + fldMM.getValue() + "." + fldYY.getValue());
    } catch (final DateTimeParseException e) {
      correct = false;
    }

    return correct;
  }

  @Override
  protected void setTitle() {
    setTitle("Rechnungsdatum");
  }

  @Override
  protected void setupInteractions() {
    getBtnOk().addActionListener(
        arg0 -> {
          if (eingabenKorrekt()) {
            Session.setDate(date);
            getCloser().windowClosing(null);
          } else {
            JOptionPane.showMessageDialog(rootPane,
                "Das eingegebene Datum ist falsch", "Fehler",
                JOptionPane.INFORMATION_MESSAGE);
          }
        });

    btnHeute.addActionListener(e -> {
      final LocalDate now = LocalDate.now();
      fldDD.setValue(now.getDayOfMonth());
      fldMM.setValue(now.getMonthValue());
      fldYY.setValue(now.getYear());
    });
  }
}
