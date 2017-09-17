package de.heinerion.betriebe.loading;

interface LoadListener {
  void notifyLoading(String message, Loadable loadable);
}
