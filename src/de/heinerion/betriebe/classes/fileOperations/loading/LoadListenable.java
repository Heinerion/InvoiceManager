package de.heinerion.betriebe.classes.fileOperations.loading;

public interface LoadListenable {
  void addListener(LoadListener listener);

  void notifyLoadListeners(String message, Loadable loadable);
}
