package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
@RequiredArgsConstructor
public class MenuFactory {
  private final DataBase dataBase;

  public JMenuBar createMenuBar(JFrame frame, PathUtilNG pathUtil) {
    return new MenuBar(frame, pathUtil, dataBase);
  }
}
