package de.heinerion.betriebe.classes.fileoperations.loading;

public interface LoadListener {
  void notifyLoading(String message, Loadable loadable);
}
