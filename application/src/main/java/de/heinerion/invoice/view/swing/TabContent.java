package de.heinerion.invoice.view.swing;

import de.heinerion.betriebe.models.Letter;
import de.heinerion.invoice.Translator;

import javax.swing.*;

public abstract class TabContent {
  private final String title;
  private final JButton delete;

  private final JPanel panel;

  protected TabContent(String aTitle) {
    panel = PanelFactory.createBackgroundPanel(PanelSides.LEFT, PanelSides.RIGHT);

    this.title = aTitle;

    delete = new JButton(Translator.translate("controls.deleteContent"));
    delete.addActionListener(e -> clear());
  }

  public JPanel getPanel() {
    return panel;
  }

  protected abstract void clear();

  public final Letter getContent() {
    return getConveyable();
  }

  protected final JButton getDeleteBtn() {
    return delete;
  }

  public final String getTitle() {
    return title;
  }

  protected abstract Letter getConveyable();

  public void refresh() {
    // Do nothing
  }
}
