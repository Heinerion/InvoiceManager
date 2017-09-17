package de.heinerion.betriebe.loading;

interface LoadListenable {
  void addListener(LoadListener listener);

  void notifyLoadListeners(String message, Loadable loadable);
}
