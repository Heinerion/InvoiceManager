package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.betriebe.models.Address;

import javax.swing.*;

public class AddressForm implements Form<Address> {
  /*
    private String apartment;
    private String company;
    private String district;
    private String location;
    private String number;
    private String postalCode;
    private String recipient;
    private String street;
  */

  @Override
  public Address getValue() {
    return null;
  }

  @Override
  public JPanel getPanel() {
    JPanel container = new JPanel();
    container.add(new JLabel("Address"));
    return container;
  }
}
