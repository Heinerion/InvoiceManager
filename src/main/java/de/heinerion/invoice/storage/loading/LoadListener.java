package de.heinerion.invoice.storage.loading;

public interface LoadListener {
  void notifyLoading(String message, Loadable loadable);
}
