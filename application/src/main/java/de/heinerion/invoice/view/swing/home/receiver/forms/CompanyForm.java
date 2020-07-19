package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.betriebe.models.Company;

import javax.swing.*;

public class CompanyForm implements Form<Company> {
  /*
    private String descriptiveName;
    private String officialName;
    private String signer;
    private String taxNumber;
    private String phoneNumber;

    private double valueAddedTax;
    private double wagesPerHour;
  */

  public CompanyForm(AddressForm addressForm, AccountForm accountForm) {
  }

  @Override
  public Company getValue() {
    return null;
  }

  @Override
  public JPanel getPanel() {
    JPanel container = new JPanel();
    container.add(new JLabel("Company"));
    return container;
  }
}
