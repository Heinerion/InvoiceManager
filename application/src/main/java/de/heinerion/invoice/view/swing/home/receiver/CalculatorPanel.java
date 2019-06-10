package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.Session;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.PanelFactory;
import de.heinerion.invoice.view.swing.PanelSides;
import de.heinerion.invoice.view.swing.home.DimensionUtil;
import de.heinerion.invoice.view.swing.home.receiver.money.Money;

import javax.swing.*;
import java.awt.*;

class CalculatorPanel {
  private static final int PERCENT = 100;

  private JPanel panel;

  private JTextField fldAmount;
  private JTextField fldNet;
  private JTextField fldVat;
  private JTextField fldGross;

  private JLabel lblNet;
  private JLabel lblVat;
  private JLabel lblGross;

  private JButton btnPlus;
  private JButton btnMinus;

  CalculatorPanel() {
    panel = PanelFactory.createBackgroundPanel(PanelSides.ALL);

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
    panel.setPreferredSize(DimensionUtil.CALCULATOR);
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
    btnPlus.addActionListener(e -> setFieldValues(addTaxes()));
    btnMinus.addActionListener(e -> setFieldValues(subtractTaxes()));
  }

  private CalculationResult addTaxes() {
    return new CalculationResult(getTaxes())
        .setNet(Money.parse(getAmount()));
  }

  private String getAmount() {
    return fldAmount.getText();
  }

  private double getTaxes() {
    return Session.getActiveCompany().getValueAddedTax() / PERCENT;
  }

  private CalculationResult subtractTaxes() {
    return new CalculationResult(getTaxes())
        .setGross(Money.parse(getAmount()));
  }

  private void setFieldValues(CalculationResult values) {
    if (values != null) {
      fldNet.setText(values.net + "");
      fldVat.setText(values.vat + "");
      fldGross.setText(values.gross + "");
    }
  }

  private class CalculationResult {
    private final double taxes;

    private Money net;
    private Money vat;
    private Money gross;

    CalculationResult(double taxes) {
      this.taxes = taxes;
    }

    CalculationResult setNet(Money amount) {
      net = amount;
      vat = net.times(taxes);
      gross = net.add(vat);

      return this;
    }

    CalculationResult setGross(Money amount) {
      gross = amount;
      net = gross.divideBy(1 + taxes);
      vat = gross.sub(net);

      return this;
    }
  }
}