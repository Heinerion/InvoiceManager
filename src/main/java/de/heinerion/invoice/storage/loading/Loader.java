package de.heinerion.invoice.storage.loading;

import java.util.List;

interface Loader extends LoadListenable {
  String getDescriptiveName();

  int getFileNumber();

  void init();

  List<Loadable> load();
}
