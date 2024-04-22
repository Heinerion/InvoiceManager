package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.listener.ActiveCompanyChangedListener;
import de.heinerion.invoice.models.Address;
import de.heinerion.invoice.repositories.AddressRepository;
import de.heinerion.invoice.view.swing.PositionCoordinates;
import de.heinerion.invoice.view.swing.home.ComponentSize;
import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;
import lombok.extern.flogger.Flogger;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * Panel to choose and edit addresses.
 * <p>
 * This Panel consists of an address selection (drop-down by name) and an address field, showing the lines of the
 * address
 */
@Flogger
class AddressChooserPanel extends JPanel implements ActiveCompanyChangedListener {
  private final transient Session session;
  private final LookAndFeelUtil lookAndFeelUtil;

  private static final int ADDRESS_FIELD_ROWS = 4;
  private static final int ADDRESS_FIELD_COLS = 1;

  private static final int SECOND_ELEMENT = 1;
  private static final int SINGLE_ELEMENT = 1;

  private static final int ANY_ROW = 0;
  private static final int INSET = 3;

  private final GridBagLayout gridLayout = new GridBagLayout();
  private transient AddressForm addressForm;
  private GridBagConstraints gridConstraints = new GridBagConstraints();

  private final transient AddressRepository addressRepository;

  /**
   * ComboBox for addresses
   */
  private JComboBox<Address> addressBox = new JComboBox<>();
  private JTextArea addressArea;

  AddressChooserPanel(AddressRepository addressRepository, Session session, LookAndFeelUtil lookAndFeelUtil) {
    this.addressRepository = addressRepository;
    this.session = session;
    this.lookAndFeelUtil = lookAndFeelUtil;

    session.addActiveCompanyListener(this);

    init();

    addAddressChooser();
    addAddressField();
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
        .withPosX(SECOND_ELEMENT)
        .withPosY(SECOND_ELEMENT)
        .withHeight(SINGLE_ELEMENT)
        .withWidth(SINGLE_ELEMENT)
        .build();

    var pnlButtons = new JPanel();
    initButtonPanel(pnlButtons);
    styleComponent(pnlButtons);
    addToGrid(pnlButtons, position);
  }

  private void initButtonPanel(JPanel pnlButtons) {
    pnlButtons.setLayout(new GridLayout(ANY_ROW, 1));
    pnlButtons.add(createSaveButton());
    pnlButtons.add(createDeleteButton());
  }

  private JButton createSaveButton() {
    var btnSave = new JButton(UIManager.getIcon(Translator.translate("icons.save")));
    btnSave.setToolTipText(Translator.translate("controls.save"));
    btnSave.addActionListener(e -> this.saveAddress());
    return btnSave;
  }

  private JButton createDeleteButton() {
    var toolTipText = Translator.translate("controls.delete");
    var btnDelete = loadImage(Translator.translate("icons.delete"))
        .map(JButton::new)
        .orElse(new JButton(toolTipText));
    btnDelete.setToolTipText(toolTipText);
    btnDelete.addActionListener(e -> this.clearAddress());
    return btnDelete;
  }

  /**
   * Loads a custom image using the class loader
   *
   * @param path path to the image, relative to src/main/resources
   *
   * @return {@link  ImageIcon} for the given path or <br> {@link Optional#empty}, if no image could be found
   */
  private static Optional<ImageIcon> loadImage(String path) {
    // Use class loader to use custom images
    java.net.URL imageURL = Thread.currentThread().getContextClassLoader().getResource(path);
    if (imageURL != null) {
      return Optional.of(new ImageIcon(imageURL));
    }

    return Optional.empty();
  }

  private void addAddressChooser() {
    // place address boxes left to their labels
    PositionCoordinates position = PositionCoordinates.builder()
        .withHeight(SINGLE_ELEMENT)
        .withWidth(SINGLE_ELEMENT)
        .build();

    JComboBox<Address> component = new JComboBox<>(new Address[1]);
    styleComponent(component);
    addToGrid(component, position);
    addressBox = component;

    position = PositionCoordinates.builder()
        .withPosX(SECOND_ELEMENT)
        .withHeight(SINGLE_ELEMENT)
        .withWidth(SINGLE_ELEMENT)
        .build();

    var newLabel = createTitleLabel();
    styleComponent(newLabel);
    addToGrid(newLabel, position);

    // apply address
    addressBox.addActionListener(e -> useGivenAddress());
  }

  private JLabel createTitleLabel() {
    var label = new JLabel(Translator.translate("address.title"));
    label.setFont(this.getFont().deriveFont(Font.BOLD));
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setLabelFor(addressBox);
    return label;
  }

  private void addAddressField() {
    addressArea = createAddressArea();
    addressForm = new AddressForm(addressArea);

    var position = PositionCoordinates.builder()
        .withPosY(SECOND_ELEMENT)
        .withHeight(SINGLE_ELEMENT)
        .withWidth(SINGLE_ELEMENT)
        .build();
    var scrollPane = new JScrollPane(addressArea);
    addToGrid(scrollPane, position);
    ComponentSize.ADDRESS_AREA.applyTo(scrollPane);
    styleComponent(scrollPane);
    setBorder(scrollPane);
  }

  private static void setBorder(JScrollPane scrollPane) {
    scrollPane.setBorder(BorderFactory.createEtchedBorder());
  }

  private JTextArea createAddressArea() {
    var area = new JTextArea(ADDRESS_FIELD_ROWS, ADDRESS_FIELD_COLS);
    area.setBackground(determineAreaBackgroundColor());
    // set opacity (although it should already be set to true)
    area.setOpaque(true);
    return area;
  }

  @Override
  public void updateUI() {
    if (addressArea != null) {
      addressArea.setBackground(determineAreaBackgroundColor());
    }
    super.updateUI();
  }

  private Color determineAreaBackgroundColor() {
    return lookAndFeelUtil.adjustColorByTheme(new JPanel().getBackground());
  }

  private void clearAddress() {
    addressForm.clear();
  }

  private void saveAddress() {
    log.atFine().log("save...");
    Address.parse(addressForm.getText())
        .ifPresent(address -> {
          session.getActiveCompany().ifPresent(address::setOwner);
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
            .findByOwner(activeCompany)
            .stream().sorted()
            .forEach(addressBox::addItem)
    );
  }

  private void useGivenAddress() {
    final Address address = (Address) this.addressBox.getSelectedItem();
    session.setActiveAddress(address);
    addressForm.setAddress(address);
  }

  private <X extends JComponent> void addToGrid(X component, PositionCoordinates coordinates) {
    updateConstraints(coordinates);
    gridLayout.setConstraints(component, gridConstraints);
    add(component);
  }

  private <X extends JComponent> void styleComponent(X component) {
    component.setOpaque(false);
    component.setForeground(getForeground());
  }

  private void updateConstraints(PositionCoordinates coordinates) {
    gridConstraints.weightx = 0;
    gridConstraints.weighty = 0;
    gridConstraints.gridx = coordinates.getPosX();
    gridConstraints.gridy = coordinates.getPosY();
    gridConstraints.gridwidth = coordinates.getWidth();
    gridConstraints.gridheight = coordinates.getHeight();
  }

  @Override
  public void notifyCompany() {
    refreshBoxes();
  }
}
