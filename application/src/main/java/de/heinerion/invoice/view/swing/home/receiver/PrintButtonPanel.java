package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.*;
import de.heinerion.invoice.print.PrintAction;
import de.heinerion.invoice.repositories.*;
import de.heinerion.invoice.view.swing.home.PanelHolder;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Flogger
@Service
public class PrintButtonPanel implements PanelHolder {
  private final Session session;
  private final SidePanel sidePanel;
  private final InvoiceRepository invoiceRepository;
  private final LetterRepository letterRepository;
  private final ItemRepository itemRepository;

  PrintButtonPanel(PrintAction printAction, InvoiceRepository invoiceRepository, LetterRepository letterRepository, ItemRepository itemRepository, Session session) {
    this.invoiceRepository = invoiceRepository;
    this.letterRepository = letterRepository;
    this.itemRepository = itemRepository;
    this.session = session;
    this.sidePanel = new SidePanel();
    sidePanel.add(createPrintButton(printAction));
  }

  private JButton createPrintButton(PrintAction printAction) {
    JButton btnDrucken = new JButton(Translator.translate("controls.print"));
    btnDrucken.setName("print");
    btnDrucken.addActionListener(printAction);
    btnDrucken.addActionListener(this::saveInvoice);
    return btnDrucken;
  }

  private void saveInvoice(ActionEvent ignored) {
    Conveyable conveyable = session.getActiveConveyable();
    if (session.getActiveConveyable() instanceof Invoice invoice) {
      log.atInfo().log("save invoice %s", invoice);
      persist(invoice);
    } else {
      log.atInfo().log("save conveyable %s", conveyable);
      letterRepository.save((Letter) conveyable);
    }
  }

  private void persist(Invoice invoice) {
    Invoice persisted = invoiceRepository.save(invoice);
    invoice.getItems().stream()
        .map(i -> i.setInvoice(persisted))
        .forEach(itemRepository::save);
  }

  @Override
  public JPanel getPanel() {
    return sidePanel;
  }
}
