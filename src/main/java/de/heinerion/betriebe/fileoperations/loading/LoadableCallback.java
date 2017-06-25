package de.heinerion.betriebe.fileoperations.loading;

@FunctionalInterface
public interface LoadableCallback {
  void continueWithResult(Loadable result);
}