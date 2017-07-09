package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.gui.ApplicationFrame;
import de.heinerion.betriebe.gui.BGPanel;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.Translator;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
public class InvoiceNumbersMenu extends AbstractMenu {
  private static final String NAME = Translator.translate("menu.invoiceNumbers");
  private BGPanel pnlNumbers;

  private Map<Company, JSpinner> numbers;

  private JLabel header;

  InvoiceNumbersMenu(ApplicationFrame origin) {
    super(origin);
  }

  @Override
  protected void addWidgets(JDialog dialog) {
    numbers = new HashMap<>();
    for (Company c : Session.getAvailableCompanies()) {
      pnlNumbers.add(new JLabel(c.getDescriptiveName()));
      SpinnerNumberModel spinnerModel = new SpinnerNumberModel(
          c.getInvoiceNumber(), 0, 500, 1);
      JSpinner numberField = new JSpinner(spinnerModel);
      numbers.put(c, numberField);
      pnlNumbers.add(numberField);
    }

    dialog.setLayout(new BorderLayout(5, 5));
    dialog.add(header, BorderLayout.PAGE_START);
    dialog.add(getBtnOk(), BorderLayout.PAGE_END);
    dialog.add(pnlNumbers, BorderLayout.CENTER);
  }

  private int calculateInvoiceNumber(String numberString) {
    int invoiceNumber = -1;

    if (isNotEmpty(numberString)) {
      invoiceNumber = Integer.parseInt(numberString);
    }

    return invoiceNumber;
  }

  private boolean isNotEmpty(String s) {
    return s != null && !"".equals(s.trim());
  }

  @Override
  protected void createWidgets() {
    header = new JLabel(Translator.translate("menu.invoiceNumbers.lastIssuedNumber"),
        SwingConstants.CENTER);

    pnlNumbers = new BGPanel(BGPanel.LEFT, BGPanel.RIGHT, BGPanel.TOP,
        BGPanel.BOTTOM);
    pnlNumbers.setLayout(new GridLayout(2, 2));
  }

  @Override
  protected void setTitle(JDialog dialog) {
    dialog.setTitle(NAME);
  }

  @Override
  String getLinkText() {
    return NAME;
  }

  @Override
  protected void setupInteractions(JDialog dialog) {
    getBtnOk().addActionListener(arg0 -> {
      for (Company c : Session.getAvailableCompanies()) {
        c.setInvoiceNumber(calculateInvoiceNumber(numbers.get(c).getValue() + ""));
      }

      getCloser().windowClosing(null);
    });
  }
}
