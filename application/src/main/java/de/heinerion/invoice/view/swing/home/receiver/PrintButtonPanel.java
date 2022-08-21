package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.repositories.InvoiceRepository;
import de.heinerion.betriebe.repositories.LetterRepository;
import de.heinerion.invoice.print.PrintAction;
import de.heinerion.invoice.view.swing.home.PanelHolder;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Flogger
@Service
public class PrintButtonPanel implements PanelHolder {
  private final SidePanel sidePanel;
  private final InvoiceRepository invoiceRepository;
  private final LetterRepository letterRepository;

  PrintButtonPanel(PrintAction printAction, InvoiceRepository invoiceRepository, LetterRepository letterRepository) {
    this.invoiceRepository = invoiceRepository;
    this.letterRepository = letterRepository;
    this.sidePanel = new SidePanel();
    sidePanel.add(createPrintButton(printAction));
  }

  private JButton createPrintButton(PrintAction printAction) {
    JButton btnDrucken = new JButton("Drucken");
    btnDrucken.setName("print");
    btnDrucken.addActionListener(printAction);
    btnDrucken.addActionListener(this::saveInvoice);
    return btnDrucken;
  }

  private void saveInvoice(ActionEvent ignored) {
    Letter letter = Session.getActiveConveyable();
    if (Session.getActiveConveyable() instanceof Invoice invoice) {
      log.atInfo().log("save invoice %s", invoice);
      invoiceRepository.save(invoice);
    } else {
      log.atInfo().log("save letter %s", letter);
      letterRepository.save(letter);
    }
  }

  @Override
  public JPanel getPanel() {
    return sidePanel;
  }
}
