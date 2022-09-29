package de.heinerion.invoice.view.swing.tab;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.listener.ConveyableListener;
import de.heinerion.invoice.models.Invoice;
import de.heinerion.invoice.view.swing.FormatUtil;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.PAGE_END;

class FooterPanel extends JPanel implements ConveyableListener {
  private final transient Session session;
  private final JLabel currentTotalNet = new JLabel();
  private final JLabel currentTotalGross = new JLabel();

  FooterPanel(JButton deleteButton, Session session) {
    super(new GridLayout(0, 1));
    this.session = session;
    session.addConveyableListener(this);

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
    if (session.getActiveConveyable() instanceof Invoice invoice) {
      currentTotalGross.setText(FormatUtil.formatLocaleDecimal(invoice
          .getGross()));
      currentTotalNet
          .setText(FormatUtil.formatLocaleDecimal(invoice.getNet()));
    }
  }
}
