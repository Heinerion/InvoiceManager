package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.listener.CompanyListener;
import de.heinerion.invoice.models.Company;

import javax.swing.*;

class CompanyButton implements CompanyListener {
  private final Session session;
  private final JButton button;
  private Company company;

  CompanyButton(Company aCompany, Session session) {
    this.session = session;
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
    return session.getActiveCompany()
        .map(c -> c.equals(this.company))
        .orElse(false);
  }

  private void registerListeners() {
    session.addCompanyListener(this);
    button.addActionListener(e -> session.setActiveCompany(this.company));
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
