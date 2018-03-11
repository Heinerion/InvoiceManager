package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.view.swing.home.Refreshable;

import javax.swing.*;

@SuppressWarnings("serial")
class CompanyChooserPanel implements Refreshable {
  private SidePanel sidePanel;
  private JPanel content = new JPanel();

  CompanyChooserPanel() {
    sidePanel = new SidePanel();
    refresh();
  }

  public void refresh() {
    sidePanel.remove(content);
    content = new JPanel();
    for (Company c : Session.getAvailableCompanies()) {
      final JButton btn = new CompanyButton(c);
      content.add(btn);
    }
    sidePanel.add(content);
  }

  @Override
  public JPanel getPanel() {
    return sidePanel;
  }
}
