package de.heinerion.betriebe.view.panels;

import javax.swing.*;

class ProgressIndicatorImpl implements ProgressIndicator {
  private final JProgressBar progressBar;

  ProgressIndicatorImpl(JProgressBar aProgressBar) {
    this.progressBar = aProgressBar;
  }

  @Override
  public int getMaximum() {
    return progressBar.getMaximum();
  }

  @Override
  public void setMaximum(int maxValue) {
    progressBar.setMaximum(maxValue);
  }

  @Override
  public String getString() {
    return progressBar.getString();
  }

  @Override
  public void setString(String string) {
    progressBar.setString(string);
  }

  @Override
  public int getValue() {
    return progressBar.getValue();
  }

  @Override
  public void setValue(int newValue) {
    progressBar.setValue(newValue);
  }

  @Override
  public void setEnabled(boolean isEnabled) {
    progressBar.setEnabled(isEnabled);
  }
}