/**
 * GemeindenMenu.java
 * heiner 30.03.2012
 */
package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.classes.gui.BGPanel;
import de.heinerion.betriebe.classes.gui.RechnungFrame;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
public class RechnungsnummernMenu extends AbstractMenu {
  private BGPanel pnlNummern;

  private Map<Company, JSpinner> numbers;

  private JLabel header;

  public RechnungsnummernMenu(RechnungFrame origin) {
    super(origin);
  }

  @Override
  protected void addWidgets() {
    numbers = new HashMap<>();
    for (Company c : Session.getAvailableCompanies()) {
      pnlNummern.add(new JLabel(c.getDescriptiveName()));
      SpinnerNumberModel spinnerModel = new SpinnerNumberModel(
          c.getInvoiceNumber(), 0, 500, 1);
      JSpinner numberField = new JSpinner(spinnerModel);
      numbers.put(c, numberField);
      pnlNummern.add(numberField);
    }

    setLayout(new BorderLayout(5, 5));
    add(header, BorderLayout.PAGE_START);
    add(getBtnOk(), BorderLayout.PAGE_END);
    add(pnlNummern, BorderLayout.CENTER);
  }

  private int calculateRn(String rnString) {
    int invoiceNumber = -1;

    if (isNotEmpty(rnString)) {
      invoiceNumber = Integer.parseInt(rnString);
    }

    return invoiceNumber;
  }

  private boolean isNotEmpty(String s) {
    return s != null && !"".equals(s.trim());
  }

  @Override
  protected void createWidgets() {
    header = new JLabel("zuletzt ausgestellte Rechnungsnr.",
        SwingConstants.CENTER);

    pnlNummern = new BGPanel(BGPanel.LEFT, BGPanel.RIGHT, BGPanel.TOP,
        BGPanel.BOTTOM);
    pnlNummern.setLayout(new GridLayout(2, 2));
  }

  @Override
  protected void setTitle() {
    setTitle("Rechnungsnummern");
  }

  @Override
  protected void setupInteractions() {
    getBtnOk().addActionListener(arg0 -> {
      for (Company c : Session.getAvailableCompanies()) {
        c.setInvoiceNumber(calculateRn(numbers.get(c).getValue() + ""));
      }

      getCloser().windowClosing(null);
    });
  }
}
