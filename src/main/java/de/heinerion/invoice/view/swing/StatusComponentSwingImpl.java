package de.heinerion.invoice.view.swing;

import de.heinerion.invoice.view.common.StatusComponent;

import javax.swing.*;
import java.awt.*;

public class StatusComponentSwingImpl implements StatusComponent<JPanel> {
  private JPanel container;
  private JProgressBar progressBar;

  StatusComponentSwingImpl() {
    this.container = new JPanel();
    initProgressBar();

    container.setLayout(new BorderLayout());
    container.add(progressBar, BorderLayout.CENTER);
  }

  private void initProgressBar() {
    this.progressBar = new JProgressBar();
    progressBar.setMinimum(0);
  }

  @Override
  public void setProgressMax(int maxValue) {
    progressBar.setMaximum(maxValue);
  }

  @Override
  public double getProgressPercentage() {
    return (double) progressBar.getValue() / progressBar.getMaximum();
  }

  @Override
  public void incrementProgress() {
    incrementProgress(1);
  }

  @Override
  public void incrementProgress(int step) {
    progressBar.setValue(progressBar.getValue() + step);
  }

  @Override
  public void initProgress() {
    progressBar.setOpaque(false);
    progressBar.setEnabled(true);
  }

  @Override
  public void disposeProgress() {
    progressBar.setEnabled(false);
    progressBar.setOpaque(true);
  }

  @Override
  public String getMessage() {
    return progressBar.getString();
  }

  @Override
  public void setMessage(String message) {
    progressBar.setString(message);
    if (!progressBar.isStringPainted()) {
      progressBar.setStringPainted(true);
    }
  }

  @Override
  public void setSize(Dimension size) {
    progressBar.setPreferredSize(size);
  }

  @Override
  public JPanel getContainer() {
    return container;
  }
}
