package de.heinerion.betriebe.view.panels.home.receiver;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.view.formatter.Formatter;
import de.heinerion.betriebe.view.panels.PositionCoordinates;
import de.heinerion.util.Translator;
import de.heinerion.util.DimensionUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
class AddressChooserPanel extends AbstractGridPanel {
  private static final String FLOPPY_SYM = "FileChooser.floppyDriveIcon";
  private static final String DELETE_SYM = "images/delete.png";

  private static final int BOLD = Font.BOLD;
  private static final int CENTER = SwingConstants.CENTER;

  private static final int ADDRESSFIELD_ROWS = 4;
  private static final int ADDRESSFIELD_COLS = 1;

  private static final int SECOND = 1;
  private static final int SIZE1 = 1;

  private static final int ANY = 0;
  private static final int INSET = 3;

  private JTextArea addressArea;

  /**
   * ComboBox für Adressen
   */
  private JComboBox<Address> addressBox = new JComboBox<>();

  private transient Formatter formatter;

  AddressChooserPanel(Formatter formatter) {
    this.formatter = formatter;

    init();

    addAddressChooser();
    addAddressField();
    addButtons();
  }

  private void init() {
    this.setOpaque(false);

    setGridConstraints(new GridBagConstraints());

    this.setLayout(getGridLayout());

    final GridBagConstraints constraints = getGridConstraints();
    // Größe der Elemente passt sich an Container an (X & Y → BOTH)
    constraints.fill = GridBagConstraints.BOTH;
    constraints.insets = new Insets(INSET, INSET, INSET, INSET);
  }

  private void addButtons() {
    PositionCoordinates position;
    position = new PositionCoordinates();
    position.setPosX(SECOND);
    position.setPosY(SECOND);
    position.setHeight(SIZE1);
    position.setWidth(SIZE1);
    final JPanel pnlButtons = this.myPanel(position);
    pnlButtons.setLayout(new GridLayout(ANY, 1));

    final JButton btnSave = new JButton(UIManager.getIcon(FLOPPY_SYM));
    btnSave.setToolTipText(Translator.translate("controls.save"));
    btnSave.addActionListener(e -> this.saveAddress());
    pnlButtons.add(btnSave);

    final ImageIcon imgDelete = PanelControl.loadImage(DELETE_SYM);
    final JButton btnDelete = (imgDelete != null) ? new JButton(imgDelete)
        : new JButton(Translator.translate("controls.delete"));
    btnDelete.setToolTipText(Translator.translate("controls.delete"));
    btnDelete.addActionListener(e -> this.clearAddress());
    pnlButtons.add(btnDelete);
  }

  private void addAddressChooser() {
    PositionCoordinates position;
    // Adressboxen links NEBEN die Labels platzieren
    position = new PositionCoordinates();
    position.setHeight(SIZE1);
    position.setWidth(SIZE1);
    addressBox = myComboBox(position, new Address[1]);

    position = new PositionCoordinates();
    position.setPosX(SECOND);
    position.setHeight(SIZE1);
    position.setWidth(SIZE1);
    final JLabel l = this.myLabel("Adresse", position, BOLD, CENTER);
    l.setLabelFor(addressBox);

    // Adresse übernehmen
    this.addressBox.addActionListener(e -> this.useGivenAddress());
  }

  private void addAddressField() {
    PositionCoordinates position;
    position = new PositionCoordinates();
    position.setPosY(SECOND);
    position.setHeight(SIZE1);
    position.setWidth(SIZE1);
    this.addressArea = this.myTextArea(position, ADDRESSFIELD_ROWS,
        ADDRESSFIELD_COLS);
    // TODO dynamische Größe für die Adressfläche?
    setSizes(this.addressArea, DimensionUtil.ADDRESS_AREA);
    this.addressArea.setBackground(Color.WHITE);
    // Undurchsichtig (sollte schon so sein)
    this.addressArea.setOpaque(true);
    this.addressArea.setBorder(BorderFactory.createEtchedBorder());
  }

  private void clearAddress() {
    this.addressArea.setText("");
  }

  private void saveAddress() {
    PanelControl.saveAddress(this.addressArea.getText());
    this.refreshBoxes();
  }

  void refreshBoxes() {
    // Alte Adressen werden gelöscht
    this.addressBox.removeAllItems();

    final Company activeCompany = Session.getActiveCompany();
    final List<Address> knownAddresses = DataBase.getAddresses(activeCompany);
    for (final Address adresse : knownAddresses) {
      this.addressBox.addItem(adresse);
    }
  }

  private void useGivenAddress() {
    final Address address = (Address) this.addressBox.getSelectedItem();
    Session.setActiveAddress(address);
    String result = null;

    if (address != null) {
      List<String> out = formatter.formatAddress(address);
      StringBuilder addressAsText = new StringBuilder();
      for (String line : out) {
        addressAsText.append(line)
            .append("\n");
      }
      result = addressAsText.toString();
    }

    final String addressText = result;
    this.addressArea.setText(addressText);
  }
}
