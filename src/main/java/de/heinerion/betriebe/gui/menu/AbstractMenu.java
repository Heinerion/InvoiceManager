package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.classes.gui.ApplicationFrame;
import de.heinerion.betriebe.gui.BusyState;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class AbstractMenu extends JDialog {

  /**
   * Generierte UID
   */
  private static final long serialVersionUID = 4454109756872082790L;
  /**
   * Quellfenster
   */
  private ApplicationFrame ursprung;
  /**
   * Der Windowadapter, für schließvorgänge zuständig
   */
  private DisposeAdapter closer = new DisposeAdapter();
  /**
   * Bestätigungs-/ Schließknopf
   */
  private JButton btnOk = new JButton("OK");
  /**
   * Knopf um Einträge zu löschen
   */
  private JButton btnDelete = new JButton("Löschen");

  /**
   * Erstellt ein Menu, macht es Modal und setzt das Ursprungsfenster
   * beschäftigt, lässt den Titel setzen und Positioniert mittig zum
   * Ursprungsfenster und immer im Vordergrund
   *
   * @param origin
   */
  public AbstractMenu(final ApplicationFrame origin) {
    super(origin, true);
    // Setze Glasspane auf ursprung
    origin.setBusyState(BusyState.BUSY);
    this.ursprung = origin;

    // Immer im Vordergrund
    this.setAlwaysOnTop(true);
    this.addWindowListener(this.closer);

    this.createWidgets();
    this.addWidgets();
    this.setupInteractions();
    this.setTitle();
    this.pack();

    this.setLocationRelativeTo(origin);

    this.setVisible(true);

  }

  protected abstract void addWidgets();

  protected abstract void createWidgets();

  public final JButton getBtnDelete() {
    return btnDelete;
  }

  public final JButton getBtnOk() {
    return btnOk;
  }

  public final DisposeAdapter getCloser() {
    return closer;
  }

  public final ApplicationFrame getUrsprung() {
    return ursprung;
  }

  public final void setBtnDelete(JButton aButton) {
    this.btnDelete = aButton;
  }

  public final void setBtnOk(JButton aButton) {
    this.btnOk = aButton;
  }

  protected abstract void setTitle();

  protected abstract void setupInteractions();

  class DisposeAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      AbstractMenu.this.dispose();
      AbstractMenu.this.ursprung.setBusyState(BusyState.IDLE);
    }
  }
}
