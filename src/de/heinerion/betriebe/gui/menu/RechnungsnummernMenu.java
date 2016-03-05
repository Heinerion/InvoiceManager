/**
 * GemeindenMenu.java
 * heiner 30.03.2012
 */
package de.heinerion.betriebe.gui.menu;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import de.heinerion.betriebe.classes.gui.BGPanel;
import de.heinerion.betriebe.classes.gui.RechnungFrame;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
public final class RechnungsnummernMenu extends AbstractMenu {
  private BGPanel pnlNummern;

  private Map<Company, JSpinner> numbers;

  private JLabel header;

  /**  */
  public RechnungsnummernMenu(final RechnungFrame origin) {
    super(origin);
  }

  @Override
  protected void addWidgets() {
    this.numbers = new HashMap<>();
    for (final Company c : Session.getAvailableCompanies()) {
      this.pnlNummern.add(new JLabel(c.getDescriptiveName()));
      final SpinnerNumberModel spinnerModel = new SpinnerNumberModel(
          c.getInvoiceNumber(), 0, 500, 1);
      final JSpinner numberField = new JSpinner(spinnerModel);
      this.numbers.put(c, numberField);
      this.pnlNummern.add(numberField);
    }

    this.setLayout(new BorderLayout(5, 5));
    this.add(this.header, BorderLayout.PAGE_START);
    this.add(getBtnOk(), BorderLayout.PAGE_END);
    this.add(this.pnlNummern, BorderLayout.CENTER);
  }

  private int calculateRn(String rnString) {
    if (!"".equals(rnString.trim())) {
      return Integer.parseInt(rnString);
    } else {
      return -1;
    }
  }

  @Override
  protected void createWidgets() {
    this.header = new JLabel("zuletzt ausgestellte Rechnungsnr.",
        SwingConstants.CENTER);

    this.pnlNummern = new BGPanel(BGPanel.LEFT, BGPanel.RIGHT, BGPanel.TOP,
        BGPanel.BOTTOM);
    this.pnlNummern.setLayout(new GridLayout(2, 2));
  }

  @Override
  protected void setTitle() {
    this.setTitle("Rechnungsnummern");
  }

  @Override
  protected void setupInteractions() {
    getBtnOk().addActionListener(arg0 -> {
      for (final Company c : Session.getAvailableCompanies()) {
        c.setInvoiceNumber(calculateRn(numbers.get(c).getValue() + ""));
      }

      getCloser().windowClosing(null);
    });
  }
}
