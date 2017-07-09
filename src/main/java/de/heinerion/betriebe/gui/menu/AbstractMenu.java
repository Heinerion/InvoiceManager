package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.gui.ApplicationFrame;
import de.heinerion.betriebe.gui.BusyState;
import de.heinerion.betriebe.services.Translator;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

abstract class AbstractMenu {

  private JDialog dialog;

  /**
   * source frame
   */
  private final ApplicationFrame origin;

  /**
   * window adapter, responsible for closing
   */
  private final DisposeAdapter closer = new DisposeAdapter();

  /**
   * confirm button
   */
  private final JButton btnOk = new JButton(Translator.translate("controls.confirm"));

  /**
   * creates an always on top modal menu and sets the origin frame busy.
   *
   * @param origin origin frame
   */
  AbstractMenu(final ApplicationFrame origin) {
    this.origin = origin;
  }

  /**
   * shows an always on top modal menu and sets the origin frame busy.
   */
  void showDialog() {
    setOriginsState(BusyState.BUSY);

    dialog = new JDialog(origin, true);
    showDialog(dialog);
  }

  private void setOriginsState(BusyState busy) {
    origin.setBusyState(busy);
  }

  private void showDialog(JDialog modalDialog) {
    modalDialog.setAlwaysOnTop(true);
    modalDialog.addWindowListener(closer);
    createWidgets();
    addWidgets(modalDialog);
    setupInteractions(modalDialog);
    setTitle(modalDialog);
    modalDialog.pack();

    modalDialog.setLocationRelativeTo(origin);
    modalDialog.setVisible(true);
  }

  abstract String getLinkText();

  protected abstract void addWidgets(JDialog dialog);

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

  protected abstract void setTitle(JDialog dialog);

  protected abstract void setupInteractions(JDialog dialog);

  class DisposeAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      dialog.dispose();
      setOriginsState(BusyState.IDLE);
    }
  }
}
