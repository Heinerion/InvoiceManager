package de.heinerion.invoice.view.common;

import javax.swing.*;
import java.awt.*;

public class StatusComponent {
  private JPanel container;
  private JProgressBar progressBar;

  public StatusComponent() {
    this.container = new JPanel();
    initProgressBar();

    container.setLayout(new BorderLayout());
    container.add(progressBar, BorderLayout.CENTER);
  }

  private void initProgressBar() {
    this.progressBar = new JProgressBar();
    progressBar.setMinimum(0);
  }

  public void setProgressMax(int maxValue) {
    progressBar.setMaximum(maxValue);
  }

  public double getProgressPercentage() {
    return (double) progressBar.getValue() / progressBar.getMaximum();
  }

  public void incrementProgress() {
    incrementProgress(1);
  }

  public void incrementProgress(int step) {
    progressBar.setValue(progressBar.getValue() + step);
  }

  public void initProgress() {
    progressBar.setOpaque(false);
    progressBar.setEnabled(true);
  }

  public void disposeProgress() {
    progressBar.setEnabled(false);
    progressBar.setOpaque(true);
  }

  public String getMessage() {
    return progressBar.getString();
  }

  public void setMessage(String message) {
    progressBar.setString(message);
    if (!progressBar.isStringPainted()) {
      progressBar.setStringPainted(true);
    }
  }

  public void setSize(Dimension size) {
    progressBar.setPreferredSize(size);
  }

  public JPanel getContainer() {
    return container;
  }
}
