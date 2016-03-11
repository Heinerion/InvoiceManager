package de.heinerion.betriebe.classes.gui;

import javax.swing.JButton;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.listener.CompanyListener;
import de.heinerion.betriebe.models.Company;

@SuppressWarnings("serial")
public final class CompanyButton extends JButton implements CompanyListener {
  private Company company;

  public CompanyButton(Company aCompany) {
    super(aCompany.getDescriptiveName());
    this.company = aCompany;

    this.setEnabled(!Session.getActiveCompany().equals(this.company));

    Session.addCompanyListener(this);
    this.addActionListener(e -> Session.setActiveCompany(this.company));
  }

  public Company getCompany() {
    return this.company;
  }

  @Override
  public void notifyCompany() {
    this.setEnabled(!Session.getActiveCompany().equals(this.company));
  }

  public void setCompany(Company aCompany) {
    this.company = aCompany;
  }
}
