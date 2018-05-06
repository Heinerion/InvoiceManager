package de.heinerion.invoice.view.swing.menu.tablemodels.archive;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.storage.loading.Loadable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public interface ArchivedInvoice extends Loadable {

  void loadFile() throws IOException;

  double getAmount();

  Company getCompany();

  LocalDate getDate();

  int getInvoiceNumber();

  String getItem();

  File getFile();

  Address getRecipient();
}
