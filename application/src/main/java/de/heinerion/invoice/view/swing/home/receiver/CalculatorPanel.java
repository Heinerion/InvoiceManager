package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.BGPanel;
import de.heinerion.invoice.view.swing.home.ComponentSize;
import de.heinerion.invoice.view.swing.home.receiver.calculator.CalculationResult;
import de.heinerion.invoice.view.swing.home.receiver.money.Money;

import javax.swing.*;
import java.awt.*;

class CalculatorPanel {
  private final Session session;

  private final JPanel panel;

  private JTextField fldAmount;
  private JTextField fldNet;
  private JTextField fldVat;
  private JTextField fldGross;

  private JLabel lblNet;
  private JLabel lblVat;
  private JLabel lblGross;

  private JButton btnPlus;
  private JButton btnMinus;

  CalculatorPanel(Session session) {
    this.session = session;

    panel = BGPanel.createWithAllSidesColored();

    initLayout();
    createWidgets();
    addWidgets();
    setupInteractions();
  }

  public JPanel getPanel() {
    return panel;
  }

  private void initLayout() {
    panel.setLayout(new BorderLayout());
    ComponentSize.CALCULATOR.applyTo(panel);
    panel.setOpaque(false);
  }

  private void createWidgets() {
    fldAmount = generateTextField();
    fldNet = generateDisabledTextField();
    fldVat = generateDisabledTextField();
    fldGross = generateDisabledTextField();

    lblNet = generateLabel("invoice.net", "calculator.netLabel");
    lblVat = generateLabel("invoice.vat", "calculator.vatLabel");
    lblGross = generateLabel("invoice.gross", "calculator.grossLabel");

    btnPlus = generateButton(Translator.translate("invoice.vat.add"));
    btnMinus = generateButton(Translator.translate("invoice.vat.sub"));
  }

  private JTextField generateTextField() {
    JTextField textField = new JTextField();
    textField.setHorizontalAlignment(SwingConstants.RIGHT);
    return textField;
  }

  private JTextField generateDisabledTextField() {
    JTextField textField = generateTextField();
    textField.setEditable(false);
    return textField;
  }

  private JButton generateButton(String label) {
    return new JButton(label);
  }

  private void addWidgets() {
    panel.add(fldAmount, BorderLayout.PAGE_START);
    panel.add(createCalculatorBody(), BorderLayout.CENTER);
  }

  private JPanel createCalculatorBody() {
    JPanel body = new JPanel();

    body.setLayout(new GridLayout(0, 2));
    body.setOpaque(false);

    body.add(btnPlus);
    body.add(btnMinus);

    body.add(lblNet);
    body.add(fldNet);

    body.add(lblVat);
    body.add(fldVat);

    body.add(lblGross);
    body.add(fldGross);

    return body;
  }

  private JLabel generateLabel(String key) {
    return new JLabel(Translator.translate(key));
  }

  private JLabel generateLabel(String key, String name) {
    JLabel label = generateLabel(key);
    label.setName(name);
    return label;
  }

  private void setupInteractions() {
    btnPlus.addActionListener(e -> setFieldValues(CalculationResult.addTaxes(getAmount(), getTaxes())));
    btnMinus.addActionListener(e -> setFieldValues(CalculationResult.subtractTaxes(getAmount(), getTaxes())));
  }

  private Money getAmount() {
    return Money.parse(fldAmount.getText());
  }

  private double getTaxes() {
    return session.getActiveCompany()
        .map(Company::getValueAddedTax)
        .orElse(Double.NaN);
  }

  private void setFieldValues(CalculationResult values) {
    if (values != null) {
      fldNet.setText(values.getNet().getValueFormatted());
      fldVat.setText(values.getVat().getValueFormatted());
      fldGross.setText(values.getGross().getValueFormatted());
    }
  }

}
