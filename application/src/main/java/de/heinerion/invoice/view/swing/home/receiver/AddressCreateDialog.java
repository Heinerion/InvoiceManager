package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.Address;
import de.heinerion.invoice.repositories.AddressRepository;
import de.heinerion.invoice.view.swing.home.receiver.forms.AddressForm;
import de.heinerion.invoice.view.swing.home.receiver.forms.*;
import de.heinerion.invoice.view.swing.menu.Menu;
import lombok.extern.flogger.Flogger;

import java.util.function.Consumer;

@Flogger
public class AddressCreateDialog extends EntityCreationDialog<Address> {

  public AddressCreateDialog(Session session, AddressRepository addressRepository, Consumer<Address> callback) {
    super(session, addressRepository, callback);
  }

  @Override
  protected AbstractForm<Address> createForm() {
    return new AddressForm();
  }

  protected String getDialogTitle() {
    return Menu.translate("address.create");
  }
}
