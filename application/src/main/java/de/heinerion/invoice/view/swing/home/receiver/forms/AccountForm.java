package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.invoice.domain.values.DvIban;
import de.heinerion.invoice.models.Account;
import de.heinerion.util.Strings;

import java.util.*;

public class AccountForm extends AbstractForm<Account> {

  private final List<FormLine<Account, ?>> properties = Arrays.asList(
      FormLine.of("name", String.class, Account::setName, Strings::isNotBlank),
      FormLine.of("iban", DvIban.class, Account::setIban, DvIban::isValid),
      FormLine.of("bic", String.class, Account::setBic, Strings::isNotBlank)
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