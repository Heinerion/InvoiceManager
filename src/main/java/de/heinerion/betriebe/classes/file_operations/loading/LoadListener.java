package de.heinerion.betriebe.classes.file_operations.loading;

public interface LoadListener {
  void notifyLoading(String message, Loadable loadable);
}
