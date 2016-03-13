package de.heinerion.betriebe.classes.fileoperations.loading;

public interface LoadListenable {
  void addListener(LoadListener listener);

  void notifyLoadListeners(String message, Loadable loadable);
}
