package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.Account;
import de.heinerion.invoice.repositories.AccountRepository;
import de.heinerion.invoice.view.swing.home.receiver.forms.*;
import de.heinerion.invoice.view.swing.menu.Menu;
import lombok.extern.flogger.Flogger;

import java.util.function.Consumer;

@Flogger
public class AccountCreateDialog extends EntityCreationDialog<Account> {

  public AccountCreateDialog(Session session, AccountRepository accountRepository, Consumer<Account> callback) {
    super(session, accountRepository, callback);
  }

  @Override
  protected AbstractForm<Account> createForm() {
    return new AccountForm();
  }

  @Override
  protected String getDialogTitle() {
    return Menu.translate("account.create");
  }
}
