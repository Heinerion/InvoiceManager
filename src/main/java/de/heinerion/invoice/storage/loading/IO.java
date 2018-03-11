package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.invoice.view.common.StatusComponent;

import java.util.List;

public interface IO {
  void saveAddresses();

  void updateTemplates(List<InvoiceTemplate> vorlagen);

  void load();

  void load(StatusComponent<?> indicator);
}
