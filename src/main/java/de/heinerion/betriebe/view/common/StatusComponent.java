package de.heinerion.betriebe.view.common;

import java.awt.*;

public interface StatusComponent<T> {
  void setProgressMax(int maxValue);
  double getProgressPercentage();
  void incrementProgress();
  void incrementProgress(int step);

  void initProgress();
  void disposeProgress();

  void setMessage(String message);
  String getMessage();

  void setSize(Dimension size);

  T getContainer();
}
