package de.heinerion.betriebe.classes.file_operations.loading;

public interface ProgressIndicator {

  int getMaximum();

  String getString();

  int getValue();

  void setEnabled(boolean isEnabled);

  void setMaximum(int maxValue);

  void setString(String string);

  void setValue(int newValue);
}
