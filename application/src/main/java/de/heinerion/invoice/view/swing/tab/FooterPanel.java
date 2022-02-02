package de.heinerion.invoice.view.swing.tab;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.listener.ConveyableListener;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.FormatUtil;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.PAGE_END;

class FooterPanel extends JPanel implements ConveyableListener {
  private final JLabel currentTotalNet = new JLabel();
  private final JLabel currentTotalGross = new JLabel();

  FooterPanel(JButton deleteButton) {
    super(new GridLayout(0, 1));
    Session.addConveyableListener(this);

    setOpaque(false);
    add(createSumPanel(), PAGE_END);
    add(deleteButton);
  }

  private JPanel createSumPanel() {
    JPanel sumPnl = new JPanel(new GridLayout(1, 0));

    sumPnl.setOpaque(false);
    sumPnl.add(createTranslatedLabel("gui.preTax"));
    sumPnl.add(currentTotalNet);
    sumPnl.add(createTranslatedLabel("gui.postTax"));
    sumPnl.add(currentTotalGross);

    return sumPnl;
  }

  private JLabel createTranslatedLabel(String translate) {
    return new JLabel(Translator.translate(translate));
  }

  @Override
  public void notifyConveyable() {
    if (Session.getActiveConveyable() instanceof Invoice invoice) {
      currentTotalGross.setText(FormatUtil.formatLocaleDecimal(invoice
          .getGross()));
      currentTotalNet
          .setText(FormatUtil.formatLocaleDecimal(invoice.getNet()));
    }
  }
}
