package de.heinerion.betriebe.view.panels.tab;

import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.view.panels.PanelFactory;
import de.heinerion.betriebe.view.panels.PanelSides;
import de.heinerion.betriebe.view.panels.TabContent;
import de.heinerion.util.Translator;

import javax.swing.*;

@SuppressWarnings("serial")
abstract class AbstractTabContent implements TabContent {
  private final String title;
  private final JButton delete;

  private JPanel panel;

  AbstractTabContent(String aTitle) {
    panel = PanelFactory.createBackgroundPanel(PanelSides.LEFT, PanelSides.RIGHT);

    this.title = aTitle;

    delete = new JButton(Translator.translate("controls.deleteContent"));
    delete.addActionListener(e -> clear());
  }

  @Override
  public JPanel getPanel() {return panel;}

  protected abstract void clear();

  @Override
  public final Letter getContent() {
    return getConveyable();
  }

  final JButton getDeleteBtn() {
    return delete;
  }

  @Override
  public final String getTitle() {
    return title;
  }

  protected abstract Letter getConveyable();

  @Override
  public void refresh() {
    // Do nothing
  }
}
