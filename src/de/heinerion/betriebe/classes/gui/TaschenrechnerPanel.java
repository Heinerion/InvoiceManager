package de.heinerion.betriebe.classes.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.tools.DimensionTool;
import de.heinerion.money.Euro;
import de.heinerion.money.Money;

@SuppressWarnings("serial")
public class TaschenrechnerPanel extends BGPanel {
  private static final int PERCENT = 100;

  public TaschenrechnerPanel() {
    super(BGPanel.LINKS, BGPanel.OBEN, BGPanel.UNTEN, BGPanel.RECHTS);
    // | Betrag |
    // [+ MWST] [- MWST (Grundpreis)]
    // + MWST → Betrag + Betrag*0,107 MWST = Betrag * 1,107
    // - MWST → Betrag - Betrag/1,107 * 0,107 = Betrag/1,107
    final JTextField fldBetrag = new JTextField();
    final JTextField fldNetto;
    final JTextField fldMWST;
    final JTextField fldBrutto;

    final JButton btnPlus;
    final JButton btnMinus;

    this.setLayout(new BorderLayout());
    this.setPreferredSize(DimensionTool.CALCULATOR);
    this.setOpaque(false);

    btnPlus = new JButton("+ MWST");
    btnMinus = new JButton("- MWST");
    fldNetto = new JTextField();
    fldMWST = new JTextField();
    fldBrutto = new JTextField();

    fldNetto.setEditable(false);
    fldMWST.setEditable(false);
    fldBrutto.setEditable(false);

    fldBetrag.setHorizontalAlignment(SwingConstants.RIGHT);
    fldNetto.setHorizontalAlignment(SwingConstants.RIGHT);
    fldMWST.setHorizontalAlignment(SwingConstants.RIGHT);
    fldBrutto.setHorizontalAlignment(SwingConstants.RIGHT);

    this.add(fldBetrag, BorderLayout.PAGE_START);
    this.add(new JPanel() {
      // Ergebnisse
      private static final long serialVersionUID = 7L;
      {
        this.setLayout(new GridLayout(0, 2));
        this.setOpaque(false);
        this.add(btnPlus);
        this.add(btnMinus);
        this.add(new JLabel("Netto"));
        this.add(fldNetto);
        this.add(new JLabel("MWST"));
        this.add(fldMWST);
        this.add(new JLabel("Brutto"));
        this.add(fldBrutto);
      }
    }, BorderLayout.CENTER);

    btnPlus.addActionListener(e -> {
      final Money[] values = this.calculate(fldBetrag.getText(), true);

      if (values != null) {
        fldNetto.setText(values[0] + "");
        fldMWST.setText(values[1] + "");
        fldBrutto.setText(values[2] + "");
      }
    });
    btnMinus.addActionListener(e -> {
      final Money[] values = this.calculate(fldBetrag.getText(), false);

      if (values != null) {
        fldNetto.setText(values[0] + "");
        fldMWST.setText(values[1] + "");
        fldBrutto.setText(values[2] + "");
      }
    });
  }

  private Money[] calculate(String text, boolean addTaxes) {
    Euro betrag;
    Euro netto;
    Euro mwst;
    Euro brutto;

    if ("".equals(text)) {
      return null;
    }

    betrag = Euro.parse(text);

    final double taxes = Session.getActiveCompany().getValueAddedTax() / PERCENT;

    if (addTaxes) {
      netto = betrag;
      mwst = netto.times(taxes);
      brutto = netto.add(mwst);
    } else {
      brutto = betrag;
      netto = brutto.divideBy(1 + taxes);
      mwst = brutto.sub(netto);
    }

    return new Money[] { netto, mwst, brutto };
  }
}
