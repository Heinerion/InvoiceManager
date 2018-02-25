package de.heinerion.betriebe.view.swing.menu;

import javax.swing.*;

interface MenuEntry {
  String getLinkText();

  void showDialog();

  void setBusyFrame(JFrame busyFrame);
}
