package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.repositories.InvoiceRepository;
import de.heinerion.invoice.print.PrintAction;
import de.heinerion.invoice.view.swing.home.PanelHolder;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
public class PrintButtonPanel implements PanelHolder {
  private SidePanel sidePanel;

  PrintButtonPanel(PrintAction printAction, InvoiceRepository invoiceRepository) {
    sidePanel = new SidePanel();

    final JButton btnDrucken = new JButton("Drucken");
    btnDrucken.setName("print");
    sidePanel.add(btnDrucken);
    btnDrucken.addActionListener(printAction);
    btnDrucken.addActionListener(e -> {
      if (Session.getActiveConveyable() instanceof Invoice invoice) {
        invoiceRepository.save(invoice);
      }
    });
  }

  @Override
  public JPanel getPanel() {
    return sidePanel;
  }
}
