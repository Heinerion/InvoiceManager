package de.heinerion.betriebe.view.swing.menu;

import javax.swing.*;

interface Menu {
  String getLinkText();

  void showDialog();

  void setBusyFrame(JFrame busyFrame);
}
