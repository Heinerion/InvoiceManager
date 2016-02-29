package de.heinerion.betriebe.classes.gui;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.tools.DimensionTool;
import de.heinerion.money.Euro;
import de.heinerion.money.Money;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class TaschenrechnerPanel extends BGPanel {
  private static final int PERCENT = 100;

  private static final String VAT = "MWST";
  public static final String NET = "Netto";
  public static final String GROSS = "Brutto";

  private JTextField fldBetrag;
  private JTextField fldNet;
  private JTextField fldVat;
  private JTextField fldGross;

  private JButton btnPlus;
  private JButton btnMinus;

  public TaschenrechnerPanel() {
    super(BGPanel.LINKS, BGPanel.OBEN, BGPanel.UNTEN, BGPanel.RECHTS);

    initLayout();
    createWidgets();
    addWidgets();
    setupInteractions();
  }

  private void initLayout() {
    setLayout(new BorderLayout());
    setPreferredSize(DimensionTool.CALCULATOR);
    setOpaque(false);
  }

  private void createWidgets() {
    fldBetrag = new JTextField();
    btnPlus = new JButton("+ " + VAT);
    btnMinus = new JButton("- " + VAT);
    fldNet = new JTextField();
    fldVat = new JTextField();
    fldGross = new JTextField();

    fldNet.setEditable(false);
    fldVat.setEditable(false);
    fldGross.setEditable(false);

    fldBetrag.setHorizontalAlignment(SwingConstants.RIGHT);
    fldNet.setHorizontalAlignment(SwingConstants.RIGHT);
    fldVat.setHorizontalAlignment(SwingConstants.RIGHT);
    fldGross.setHorizontalAlignment(SwingConstants.RIGHT);
  }

  private void addWidgets() {
    add(fldBetrag, BorderLayout.PAGE_START);
    add(createCalculatorBody(), BorderLayout.CENTER);
  }

  private JPanel createCalculatorBody() {
    JPanel innerPanel = new JPanel();

    innerPanel.setLayout(new GridLayout(0, 2));
    innerPanel.setOpaque(false);
    innerPanel.add(btnPlus);
    innerPanel.add(btnMinus);
    innerPanel.add(new JLabel(NET));
    innerPanel.add(fldNet);
    innerPanel.add(new JLabel(VAT));
    innerPanel.add(fldVat);
    innerPanel.add(new JLabel(GROSS));
    innerPanel.add(fldGross);

    return innerPanel;
  }

  private void setupInteractions() {
    btnPlus.addActionListener(this::addVat);
    btnMinus.addActionListener(this::subVat);
  }

  private void addVat(ActionEvent e) {
    final Money[] values = calculate(fldBetrag.getText(), true);

    setFieldValues(values);
  }

  private void subVat(ActionEvent e) {
    final Money[] values = calculate(fldBetrag.getText(), false);

    setFieldValues(values);
  }

  private Money[] calculate(String text, boolean addTaxes) {
    if (isEmpty(text)) {
      return null;
    }

    Euro amount = Euro.parse(text);

    final double taxes = Session.getActiveCompany().getValueAddedTax() / PERCENT;

    Euro net;
    Euro vat;
    Euro gross;

    if (addTaxes) {
      net = amount;
      vat = net.times(taxes);
      gross = net.add(vat);
    } else {
      gross = amount;
      net = gross.divideBy(1 + taxes);
      vat = gross.sub(net);
    }

    return new Money[]{net, vat, gross};
  }

  private boolean isEmpty(String text) {
    return text == null || text.isEmpty() || "".equals(text.trim());
  }

  private void setFieldValues(Money[] values) {
    if (values != null) {
      fldNet.setText(values[0] + "");
      fldVat.setText(values[1] + "");
      fldGross.setText(values[2] + "");
    }
  }
}
