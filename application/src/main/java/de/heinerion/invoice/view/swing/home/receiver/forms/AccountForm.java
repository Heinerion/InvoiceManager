package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.betriebe.models.Account;

import java.util.Arrays;
import java.util.List;

public class AccountForm extends AbstractForm<Account> {

  private List<FormLine<Account, ?>> properties = Arrays.asList(
      FormLine.builder(Account.class, String.class).name("name").setter(Account::setName).valid(s -> !s.isEmpty()).build(),
      FormLine.builder(Account.class, String.class).name("iban").setter(Account::setIban).valid(s -> !s.isEmpty()).build(),
      FormLine.builder(Account.class, String.class).name("bic").setter(Account::setBic).valid(s -> !s.isEmpty()).build()
  );


  @Override
  protected Account createInstance() {
    return new Account();
  }

  @Override
  public List<FormLine<Account, ?>> getProperties() {
    return properties;
  }

  @Override
  public String getTitle() {
    return "Account";
  }
}