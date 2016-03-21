package de.heinerion.betriebe.fileoperations.loading;

public interface LoadListenable {
  void addListener(LoadListener listener);

  void notifyLoadListeners(String message, Loadable loadable);
}
