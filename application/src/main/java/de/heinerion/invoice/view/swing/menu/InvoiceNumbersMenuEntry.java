package de.heinerion.invoice.view.swing.menu;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.repositories.CompanyRepository;
import de.heinerion.invoice.view.swing.BGPanel;
import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.util.*;

@RequiredArgsConstructor
class InvoiceNumbersMenuEntry extends MenuEntry {
  private static final String NAME = Menu.translate("invoiceNumbers");

  private final Session session;
  private final CompanyRepository companyRepository;
  private final LookAndFeelUtil lookAndFeelUtil;

  private JPanel pnlNumbers;

  private Map<Company, JSpinner> numbers;

  private JLabel header;

  @Override
  protected void addWidgets(JDialog dialog) {
    numbers = new HashMap<>();
    for (Company c : companyRepository.findAll().stream().sorted().toList()) {
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
    header = new JLabel(Menu.translate("invoiceNumbers.lastIssuedNumber"),
        SwingConstants.CENTER);

    pnlNumbers = BGPanel.createWithAllSidesColored(lookAndFeelUtil);
    pnlNumbers.setLayout(new GridLayout(0, 2));
  }

  @Override
  protected void setTitle(JDialog dialog) {
    dialog.setTitle(NAME);
  }

  @Override
  public String getLinkText() {
    return NAME;
  }

  @Override
  protected void setupInteractions(JDialog dialog) {
    getBtnOk().addActionListener(arg0 -> {
      for (Company company : companyRepository.findAll()) {
        int invoiceNumber = company.getInvoiceNumber();
        int guiNumber = calculateInvoiceNumber(numbers.get(company).getValue() + "");
        if (invoiceNumber != guiNumber) {
          company.setInvoiceNumber(guiNumber);
          Company persisted = companyRepository.save(company);
          if (session.isActiveCompany(persisted)) {
            session.setActiveCompany(persisted);
          }
        }
      }

      getCloser().windowClosing(null);
    });
  }
}
