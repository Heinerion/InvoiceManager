package de.heinerion.invoice.storage.loading;

interface LoadListenable {
  void addListener(LoadListener listener);

  void notifyLoadListeners(String message, Loadable loadable);
}
