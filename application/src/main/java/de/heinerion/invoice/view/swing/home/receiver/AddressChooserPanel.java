package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.repositories.address.AddressRepository;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.formatter.Formatter;
import de.heinerion.invoice.view.swing.PositionCoordinates;
import de.heinerion.invoice.view.swing.home.ComponentSize;
import lombok.extern.flogger.Flogger;

import javax.swing.*;
import java.awt.*;

@Flogger
class AddressChooserPanel extends JPanel {
  private final transient Session session;

  private static final int BOLD = Font.BOLD;
  private static final int CENTER = SwingConstants.CENTER;

  private static final int ADDRESS_FIELD_ROWS = 4;
  private static final int ADDRESS_FIELD_COLS = 1;

  private static final int SECOND = 1;
  private static final int SIZE1 = 1;

  private static final int ANY = 0;
  private static final int INSET = 3;

  private final GridBagLayout gridLayout = new GridBagLayout();
  private transient AddressForm addressForm;
  private GridBagConstraints gridConstraints = new GridBagConstraints();

  private final transient AddressRepository addressRepository;

  /**
   * ComboBox for addresses
   */
  private JComboBox<Address> addressBox = new JComboBox<>();

  AddressChooserPanel(Formatter formatter, AddressRepository addressRepository, Session session) {
    this.addressRepository = addressRepository;
    this.session = session;

    init();

    addAddressChooser();
    addAddressField(formatter);
    addButtons();
  }

  private void init() {
    setOpaque(false);

    gridConstraints = new GridBagConstraints();
    setLayout(gridLayout);

    // size of elements adapts to container (X & Y → BOTH)
    gridConstraints.fill = GridBagConstraints.BOTH;
    gridConstraints.insets = new Insets(INSET, INSET, INSET, INSET);
  }

  private void addButtons() {
    PositionCoordinates position = PositionCoordinates.builder()
        .withPosX(SECOND)
        .withPosY(SECOND)
        .withHeight(SIZE1)
        .withWidth(SIZE1)
        .build();

    final JPanel pnlButtons = create(new JPanel(), position);
    pnlButtons.setLayout(new GridLayout(ANY, 1));

    final JButton btnSave = new JButton(UIManager.getIcon(Translator.translate("icons.save")));
    btnSave.setToolTipText(Translator.translate("controls.save"));
    btnSave.addActionListener(e -> this.saveAddress());
    pnlButtons.add(btnSave);

    final ImageIcon imgDelete = PanelControl.loadImage(Translator.translate("icons.delete"));
    String toolTipText = Translator.translate("controls.delete");
    final JButton btnDelete = (imgDelete != null) ? new JButton(imgDelete) : new JButton(toolTipText);
    btnDelete.setToolTipText(toolTipText);
    btnDelete.addActionListener(e -> this.clearAddress());
    pnlButtons.add(btnDelete);
  }

  private void addAddressChooser() {
    // place address boxes left to their labels
    PositionCoordinates position = PositionCoordinates.builder()
        .withHeight(SIZE1)
        .withWidth(SIZE1)
        .build();

    addressBox = create(new JComboBox<>(new Address[1]), position);

    position = PositionCoordinates.builder()
        .withPosX(SECOND)
        .withHeight(SIZE1)
        .withWidth(SIZE1)
        .build();

    final JLabel newLabel = new JLabel(Translator.translate("address.title"));
    newLabel.setFont(this.getFont().deriveFont(BOLD));
    newLabel.setHorizontalAlignment(CENTER);

    final JLabel l = create(newLabel, position);
    l.setLabelFor(addressBox);

    // apply address
    addressBox.addActionListener(e -> useGivenAddress());
  }

  private void addAddressField(Formatter formatter) {
    PositionCoordinates position;
    position = PositionCoordinates.builder()
        .withPosY(SECOND)
        .withHeight(SIZE1)
        .withWidth(SIZE1)
        .build();
    addressForm = new AddressForm(formatter);
    JTextArea addressArea = create(new JTextArea(ADDRESS_FIELD_ROWS, ADDRESS_FIELD_COLS), position);
    ComponentSize.ADDRESS_AREA.applyTo(addressArea);
    addressArea.setBackground(Color.WHITE);
    // set opacity (although it should already be set to true)
    addressArea.setOpaque(true);
    addressArea.setBorder(BorderFactory.createEtchedBorder());
    addressForm.setAddressArea(addressArea);
  }

  private void clearAddress() {
    addressForm.clear();
  }

  private void saveAddress() {
    log.atFine().log("save...");
    PanelControl.parseAddress(addressForm.getText())
        .ifPresent(address -> {
          addressRepository.save(address);
          log.atFine().log("saved %s", address);
          refreshBoxes();
        });
  }

  void refreshBoxes() {
    // delete old addresses
    addressBox.removeAllItems();

    session.getActiveCompany().ifPresent(activeCompany ->
        addressRepository
            .findByCompany(activeCompany)
            .forEach(addressBox::addItem)
    );
  }

  private void useGivenAddress() {
    final Address address = (Address) this.addressBox.getSelectedItem();
    session.setActiveAddress(address);
    addressForm.setAddress(address);
  }

  /**
   * Creates a new component for the grid.<br> This method is to be called internally only and is used to streamline the
   * creation of distinct components
   *
   * @param component
   *     the component to be placed in the grid
   *
   * @return the created component for further customization
   */
  private <X extends JComponent> X create(X component, PositionCoordinates coordinates) {
    gridConstraints.weightx = 0;
    gridConstraints.weighty = 0;
    gridConstraints.gridx = coordinates.getPosX();
    gridConstraints.gridy = coordinates.getPosY();
    gridConstraints.gridwidth = coordinates.getWidth();
    gridConstraints.gridheight = coordinates.getHeight();

    gridLayout.setConstraints(component, gridConstraints);
    component.setOpaque(false);
    component.setForeground(getForeground());
    add(component);
    return component;
  }
}
