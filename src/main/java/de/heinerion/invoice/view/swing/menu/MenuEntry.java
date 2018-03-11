package de.heinerion.invoice.view.swing.menu;

import javax.swing.*;

interface MenuEntry {
  String getLinkText();

  void showDialog();

  void setBusyFrame(JFrame busyFrame);
}
