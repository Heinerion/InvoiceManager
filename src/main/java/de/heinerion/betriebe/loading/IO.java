package de.heinerion.betriebe.loading;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.view.swing.ProgressIndicator;

import java.util.List;

public interface IO {
  void saveAddresses();

  void updateTemplates(List<InvoiceTemplate> vorlagen);

  void load();

  void load(ProgressIndicator indicator);
}
