package de.heinerion.invoice.storage.loading;

@FunctionalInterface
interface LoadableCallback {
  void continueWithResult(Loadable result);
}