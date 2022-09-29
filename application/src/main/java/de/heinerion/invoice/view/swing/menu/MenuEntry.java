package de.heinerion.invoice.view.swing.menu;

import de.heinerion.invoice.Translator;

import javax.swing.*;
import java.awt.event.*;

public abstract class MenuEntry {
  /**
   * window adapter, responsible for closing
   */
  private final DisposeAdapter closer = new DisposeAdapter();
  /**
   * confirm button
   */
  private final JButton btnOk = new JButton(Translator.translate("controls.confirm"));
  /**
   * source frame
   */
  private BusyFrame busyFrame;
  private JDialog dialog;

  public abstract String getLinkText();

  /**
   * shows an always on top modal menu and sets the origin frame busy.
   */
  void showDialog() {
    dialog = new JDialog(busyFrame.frame());
    showDialog(dialog);
  }

  private void showDialog(JDialog dialog) {
    dialog.addWindowListener(closer);
    dialog.setAlwaysOnTop(false);
    createWidgets();
    addWidgets(dialog);
    setupInteractions(dialog);
    setTitle(dialog);
    dialog.pack();

    dialog.setVisible(true);
  }

  protected abstract void addWidgets(JDialog dialog);

  protected abstract void createWidgets();

  protected final JButton getBtnOk() {
    return btnOk;
  }

  protected final DisposeAdapter getCloser() {
    return closer;
  }

  final JFrame getBusyFrame() {
    return busyFrame.frame();
  }

  void setBusyFrame(JFrame frame) {
    this.busyFrame = new BusyFrame(frame);
  }

  protected abstract void setTitle(JDialog dialog);

  protected abstract void setupInteractions(JDialog dialog);

  protected final class DisposeAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      dialog.dispose();
      busyFrame.setBusy(false);
    }
  }
}
