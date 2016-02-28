package de.heinerion.betriebe.gui.content;

import javax.swing.JButton;

import de.heinerion.betriebe.classes.gui.BGPanel;
import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.models.interfaces.Conveyable;

@SuppressWarnings("serial")
public abstract class AbstractTabContent extends BGPanel implements TabContent {
  private String title;
  private JButton delete;
  private Conveyable content;

  protected AbstractTabContent(String aTitle) {
    super(BGPanel.LINKS, BGPanel.RECHTS);

    this.title = aTitle;

    delete = new JButton(Constants.BUTTON_DELETE_CONTENTS);
    delete.addActionListener(e -> clear());
  }

  protected abstract void clear();

  @Override
  public final Conveyable getContent() {
    content = getConveyable();
    return content;
  }

  public final JButton getDelete() {
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
