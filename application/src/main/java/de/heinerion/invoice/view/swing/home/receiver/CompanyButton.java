package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.view.swing.CompanyListener;

import javax.swing.*;

class CompanyButton implements CompanyListener {
  private Company company;
  private JButton button;

  CompanyButton(Company aCompany) {
    button = new JButton(aCompany.getDescriptiveName());
    setCompany(aCompany);
    updateEnabledState();
    registerListeners();
  }

  private void setCompany(Company aCompany) {
    this.company = aCompany;
  }

  private void updateEnabledState() {
    button.setEnabled(!isCurrentlySelected());
  }

  private boolean isCurrentlySelected() {
    return Session.getActiveCompany().equals(this.company);
  }

  private void registerListeners() {
    Session.addCompanyListener(this);
    button.addActionListener(e -> Session.setActiveCompany(this.company));
  }

  public JButton getButton() {
    return button;
  }

  @Override
  public void notifyCompany() {
    updateEnabledState();
  }

  public Company getCompany() {
    return this.company;
  }
}
