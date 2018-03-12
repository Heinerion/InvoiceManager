package de.heinerion.invoice.view.swing.menu;

import de.heinerion.invoice.Translator;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class AbstractMenuEntry implements MenuEntry {
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

  /**
   * shows an always on top modal menu and sets the origin frame busy.
   */
  @Override
  public void showDialog() {
    busyFrame.setBusy(true);

    dialog = new JDialog(busyFrame.getFrame(), true);
    showDialog(dialog);
  }

  private void showDialog(JDialog modalDialog) {
    modalDialog.setAlwaysOnTop(true);
    modalDialog.addWindowListener(closer);
    createWidgets();
    addWidgets(modalDialog);
    setupInteractions(modalDialog);
    setTitle(modalDialog);
    modalDialog.pack();

    modalDialog.setLocationRelativeTo(busyFrame.getFrame());
    modalDialog.setVisible(true);
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
    return busyFrame.getFrame();
  }

  @Override
  public void setBusyFrame(JFrame frame) {
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
