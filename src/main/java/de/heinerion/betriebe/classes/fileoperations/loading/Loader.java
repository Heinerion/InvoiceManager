package de.heinerion.betriebe.classes.fileoperations.loading;

import java.util.List;

public interface Loader<T> extends LoadListenable {
  String getDescriptiveName();

  int getFileNumber();

  void init();

  List<Loadable> load();
}