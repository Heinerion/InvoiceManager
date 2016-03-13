package de.heinerion.betriebe.classes.fileoperations.loading;

import javax.swing.JProgressBar;

public final class JProgressBarIndicator implements ProgressIndicator {
  private final JProgressBar progressBar;

  public JProgressBarIndicator(JProgressBar aProgressBar) {
    this.progressBar = aProgressBar;
  }

  @Override
  public int getMaximum() {
    return progressBar.getMaximum();
  }

  @Override
  public String getString() {
    return progressBar.getString();
  }

  @Override
  public int getValue() {
    return progressBar.getValue();
  }

  @Override
  public void setEnabled(boolean isEnabled) {
    progressBar.setEnabled(isEnabled);
  }

  @Override
  public void setMaximum(int maxValue) {
    progressBar.setMaximum(maxValue);
  }

  @Override
  public void setString(String string) {
    progressBar.setString(string);
  }

  @Override
  public void setValue(int newValue) {
    progressBar.setValue(newValue);
  }
}