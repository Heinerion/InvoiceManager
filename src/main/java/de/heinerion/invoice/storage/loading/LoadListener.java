package de.heinerion.invoice.storage.loading;

interface LoadListener {
  void notifyLoading(String message, Loadable loadable);
}
