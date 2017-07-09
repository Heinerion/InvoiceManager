package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.gui.ApplicationFrame;
import de.heinerion.betriebe.gui.BusyState;
import de.heinerion.betriebe.services.Translator;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class AbstractMenu {

  JDialog dialog;

  /**
   * Generated UID
   */
  private static final long serialVersionUID = 4454109756872082790L;
  /**
   * source frame
   */
  private ApplicationFrame origin;
  /**
   * window adapter, responsible for closing
   */
  private DisposeAdapter closer = new DisposeAdapter();
  /**
   * confirm button
   */
  private JButton btnOk = new JButton(Translator.translate("controls.confirm"));

  /**
   * creates an always on top modal menu and sets the origin frame busy.
   *
   * @param origin origin frame
   */
  AbstractMenu(final ApplicationFrame origin) {
    dialog = new JDialog(origin, true);
    // create glass pane on top of the origin frame
    origin.setBusyState(BusyState.BUSY);
    this.origin = origin;

    dialog.setAlwaysOnTop(true);
    dialog.addWindowListener(closer);

    createWidgets();
    addWidgets();
    setupInteractions();
    setTitle();
    dialog.pack();

    dialog.setLocationRelativeTo(origin);

    dialog.setVisible(true);
  }

  protected abstract void addWidgets();

  protected abstract void createWidgets();

  final JButton getBtnOk() {
    return btnOk;
  }

  final DisposeAdapter getCloser() {
    return closer;
  }

  final ApplicationFrame getOrigin() {
    return origin;
  }

  protected abstract void setTitle();

  protected abstract void setupInteractions();

  class DisposeAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      AbstractMenu.this.dialog.dispose();
      AbstractMenu.this.origin.setBusyState(BusyState.IDLE);
    }
  }
}
