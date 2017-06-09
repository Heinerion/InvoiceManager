package de.heinerion.betriebe.gui.panels;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.gui.CompanyButton;
import de.heinerion.betriebe.models.Company;

import javax.swing.*;

@SuppressWarnings("serial")
public final class CompanyChooserPanel extends SidePanel {
  private JPanel content = new JPanel();

  public CompanyChooserPanel() {
    refresh();
  }

  public void refresh() {
    remove(content);
    content = new JPanel();
    for (Company c : Session.getAvailableCompanies()) {
      final JButton btn = new CompanyButton(c);
      content.add(btn);
    }
    add(content);
  }
}
