package de.heinerion.betriebe.gui.menu;

import javax.swing.*;

interface Menu {
  String getLinkText();

  void showDialog();

  void setBusyFrame(JFrame busyFrame);
}
