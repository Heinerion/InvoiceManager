package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.*;
import de.heinerion.invoice.view.swing.home.receiver.forms.*;
import de.heinerion.invoice.view.swing.menu.Menu;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

import javax.swing.*;

@Flogger
public class CompanyCreateDialog extends EntityCreationDialog<Company> {
  private final Session session;

  @Getter
  private final JButton button;

  private final AccountRepository accountRepository;
  private final AddressRepository addressRepository;
  private Account chosenAccount;
  private Address address;

  public CompanyCreateDialog(Session session, CompanyRepository companyRepository, AccountRepository accountRepository, AddressRepository addressRepository) {
    super(session, companyRepository, ignored -> session.notifyAvailableCompanies());
    this.session = session;
    this.accountRepository = accountRepository;
    this.addressRepository = addressRepository;

    button = new JButton("+");
    button.addActionListener(ignored -> showDialog());
  }

  @Override
  protected AbstractForm<Company> createForm() {
    return new CompanyForm();
  }

  @Override
  protected String getDialogTitle() {
    return Menu.translate("companies.create");
  }

  @Override
  public void showDialog() {
    new AccountCreateDialog(session, accountRepository, acc -> {
      setAccount(acc);
      new AddressCreateDialog(session, addressRepository, addr -> {
        setAddress(addr);
        super.showDialog();
      }).showDialog();
    }).showDialog();
  }

  private void setAddress(Address address) {
    this.address = address;
  }

  private void setAccount(Account chosenAccount) {
    this.chosenAccount = chosenAccount;
  }

  @Override
  protected Company getEntityFromForm() {
    Company company = super.getEntityFromForm();
    company.setAccount(chosenAccount);
    company.setAddress(address);
    return company;
  }
}
