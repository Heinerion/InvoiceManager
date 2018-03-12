package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.view.swing.CompanyListener;

import javax.swing.*;

@SuppressWarnings("serial")
class CompanyButton extends JButton implements CompanyListener {
  private transient Company company;

  CompanyButton(Company aCompany) {
    super(aCompany.getDescriptiveName());
    setCompany(aCompany);
    updateEnabledState();
    registerListeners();
  }

  private void setCompany(Company aCompany) {
    this.company = aCompany;
  }

  private void updateEnabledState() {
    setEnabled(!isCurrentlySelected());
  }

  private boolean isCurrentlySelected() {
    return Session.getActiveCompany().equals(this.company);
  }

  private void registerListeners() {
    Session.addCompanyListener(this);
    addActionListener(e -> Session.setActiveCompany(this.company));
  }

  @Override
  public void notifyCompany() {
    updateEnabledState();
  }

  public Company getCompany() {
    return this.company;
  }
}
