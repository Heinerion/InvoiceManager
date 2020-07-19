package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.view.swing.home.Refreshable;

import javax.swing.*;

class CompanyChooserPanel implements Refreshable {
  private SidePanel sidePanel;
  private CompanyCreateDialog companyCreateDialog;
  private JPanel content = new JPanel();

  CompanyChooserPanel(CompanyCreateDialog companyCreateDialog) {
    this.sidePanel = new SidePanel();
    this.companyCreateDialog = companyCreateDialog;
    refresh();
  }

  public void refresh() {
    sidePanel.remove(content);
    content = new JPanel();
    for (Company c : Session.getAvailableCompanies()) {
      content.add(new CompanyButton(c).getButton());
    }
    content.add(companyCreateDialog.getButton());
    sidePanel.add(content);
  }

  @Override
  public JPanel getPanel() {
    return sidePanel;
  }
}
