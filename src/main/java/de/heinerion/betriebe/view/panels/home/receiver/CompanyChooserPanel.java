package de.heinerion.betriebe.view.panels.home.receiver;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.view.panels.home.Refreshable;

import javax.swing.*;

@SuppressWarnings("serial")
class CompanyChooserPanel extends SidePanel implements Refreshable {
  private JPanel content = new JPanel();

  CompanyChooserPanel() {
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

  @Override
  public JPanel getPanel() {
    return this;
  }
}
