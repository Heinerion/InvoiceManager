package de.heinerion.betriebe.classes.fileOperations.loading;

public interface LoadListener {
  void notifyLoading(String message, Loadable loadable);
}
