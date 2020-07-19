package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.betriebe.models.Account;

import javax.swing.*;

public class AccountForm implements Form<Account> {
  /*
    private String bic;
    private String iban;
    private String name;
  */

  @Override
  public Account getValue() {
    return null;
  }

  @Override
  public JPanel getPanel() {
    JPanel container = new JPanel();
    container.add(new JLabel("Account"));
    return container;
  }
}
