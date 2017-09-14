package de.heinerion.betriebe.gui;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.util.DimensionUtil;
import de.heinerion.money.Euro;
import de.heinerion.money.Money;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class CalculatorPanel extends BGPanel {
  private static final int PERCENT = 100;

  private JTextField fldAmount;
  private JTextField fldNet;
  private JTextField fldVat;
  private JTextField fldGross;

  private JLabel lblNet;
  private JLabel lblVat;
  private JLabel lblGross;

  private JButton btnPlus;
  private JButton btnMinus;

  public CalculatorPanel() {
    super(LEFT, TOP, BOTTOM, RIGHT);

    initLayout();
    createWidgets();
    addWidgets();
    setupInteractions();
  }

  private void initLayout() {
    setLayout(new BorderLayout());
    setPreferredSize(DimensionUtil.CALCULATOR);
    setOpaque(false);
  }

  private void createWidgets() {
    fldAmount = generateTextField();
    fldNet = generateDisabledTextField();
    fldVat = generateDisabledTextField();
    fldGross = generateDisabledTextField();

    lblNet = generateLabel("invoice.net", "calculator.netLabel");
    lblVat = generateLabel("invoice.vat");
    lblGross = generateLabel("invoice.gross");

    String vat = Translator.translate("invoice.vat");
    btnPlus = generateButton("+ " + vat);
    btnMinus = generateButton("- " + vat);
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
    add(fldAmount, BorderLayout.PAGE_START);
    add(createCalculatorBody(), BorderLayout.CENTER);
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
    btnMinus.addActionListener(e -> setFieldValues(substractTaxes()));
  }

  private CalculationResult addTaxes() {
    return new CalculationResult(getTaxes())
        .setNet(Euro.parse(getAmount()));
  }

  private String getAmount() {
    return fldAmount.getText();
  }

  private double getTaxes() {
    return Session.getActiveCompany().getValueAddedTax() / PERCENT;
  }

  private CalculationResult substractTaxes() {
    return new CalculationResult(getTaxes())
        .setGross(Euro.parse(getAmount()));
  }

  private void setFieldValues(CalculationResult values) {
    if (values != null) {
      fldNet.setText(values.net + "");
      fldVat.setText(values.vat + "");
      fldGross.setText(values.gross + "");
    }
  }

  private class CalculationResult {
    private double taxes;

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