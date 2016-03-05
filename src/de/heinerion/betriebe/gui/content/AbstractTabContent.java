package de.heinerion.betriebe.gui.content;

import de.heinerion.betriebe.classes.gui.BGPanel;
import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.models.interfaces.Conveyable;

import javax.swing.*;

@SuppressWarnings("serial")
public abstract class AbstractTabContent extends BGPanel implements TabContent {
  private String title;
  private JButton delete;

  protected AbstractTabContent(String aTitle) {
    super(BGPanel.LEFT, BGPanel.RIGHT);

    this.title = aTitle;

    delete = new JButton(Constants.BUTTON_DELETE_CONTENTS);
    delete.addActionListener(e -> clear());
  }

  protected abstract void clear();

  @Override
  public final Conveyable getContent() {
    return getConveyable();
  }

  public final JButton getDeleteBtn() {
    return delete;
  }

  @Override
  public final String getTitle() {
    return title;
  }

  protected abstract Conveyable getConveyable();

  @Override
  public void refresh() {
    // Do nothing
  }
}
