package de.heinerion.betriebe.fileoperations.loading;

public interface LoadListener {
  void notifyLoading(String message, Loadable loadable);
}
