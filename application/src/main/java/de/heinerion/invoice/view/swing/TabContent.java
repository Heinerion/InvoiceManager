package de.heinerion.invoice.view.swing;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.models.Conveyable;
import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;

import javax.swing.*;

public abstract class TabContent {
  private final String title;
  private final JButton delete;

  private final JPanel panel;

  protected TabContent(LookAndFeelUtil lookAndFeelUtil, String aTitle) {
    panel = BGPanel.createWithColoredSides(lookAndFeelUtil, PanelSides.LEFT, PanelSides.RIGHT);

    this.title = aTitle;

    delete = new JButton(Translator.translate("controls.deleteContent"));
    delete.addActionListener(e -> clear());
  }

  public JPanel getPanel() {
    return panel;
  }

  protected abstract void clear();

  public final Conveyable getContent() {
    return getConveyable();
  }

  protected final JButton getDeleteBtn() {
    return delete;
  }

  public final String getTitle() {
    return title;
  }

  protected abstract Conveyable getConveyable();

  public void refresh() {
    // Do nothing
  }
}
