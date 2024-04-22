package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.invoice.domain.values.DvIban;
import de.heinerion.invoice.models.Account;

import java.util.*;

import static de.heinerion.invoice.view.swing.home.receiver.forms.ComponentFactory.createConverterComponent;

public class AccountForm extends AbstractForm<Account> {

  private final List<FormLine<Account, ?, ?>> properties = Arrays.asList(
      FormLine.ofString("name", Account::setName),
      FormLine.of("iban", Account::setIban, DvIban::isValid, createConverterComponent(DvIban.MAX_LEN, DvIban::of)),
      FormLine.ofString("bic", Account::setBic)
  );

  @Override
  protected Account createInstance() {
    return new Account();
  }

  @Override
  public List<FormLine<Account, ?, ?>> getProperties() {
    return properties;
  }

  @Override
  public String getTitle() {
    return "Account";
  }
}