package de.heinerion.betriebe.loading;

@FunctionalInterface
interface LoadableCallback {
  void continueWithResult(Loadable result);
}