package de.heinerion.betriebe.gui.panels;

import de.heinerion.betriebe.classes.gui.AbstractGridPanel;
import de.heinerion.betriebe.classes.gui.PositionCoordinates;
import de.heinerion.betriebe.classes.gui.panels.PanelControl;
import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.tools.DimensionTool;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public final class AddressChooserPanel extends AbstractGridPanel {
  private static final String FLOPPY_SYM = "FileChooser.floppyDriveIcon";
  private static final String DELETE_SYM = "de/heinerion/betriebe/images/delete.png";

  private static final int BOLD = Font.BOLD;
  private static final int CENTER = SwingConstants.CENTER;

  private static final int ADDRESSFIELD_ROWS = 4;
  private static final int ADDRESSFIELD_COLS = 1;

  private static final int SECOND = 1;
  private static final int SIZE1 = 1;

  private static final int ANY = 0;
  private static final int INSET = 3;

  private JTextArea areaAdresse;

  /**
   * ComboBox für Adressen
   */
  private JComboBox<Address> boxAdressen = new JComboBox<>();

  public AddressChooserPanel() {
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
    btnSave.setToolTipText(Constants.BUTTON_SAVE);
    btnSave.addActionListener(e -> this.saveAddress());
    pnlButtons.add(btnSave);

    final ImageIcon imgDelete = PanelControl.loadImage(DELETE_SYM);
    final JButton btnDelete = (imgDelete != null) ? new JButton(imgDelete)
        : new JButton(Constants.BUTTON_DELETE);
    btnDelete.setToolTipText(Constants.BUTTON_DELETE);
    btnDelete.addActionListener(e -> this.clearAddress());
    pnlButtons.add(btnDelete);
  }

  private void addAddressChooser() {
    PositionCoordinates position;
    // Adressboxen links NEBEN die Labels platzieren
    position = new PositionCoordinates();
    position.setHeight(SIZE1);
    position.setWidth(SIZE1);
    boxAdressen = myComboBox(position, new Address[1]);

    position = new PositionCoordinates();
    position.setPosX(SECOND);
    position.setHeight(SIZE1);
    position.setWidth(SIZE1);
    final JLabel l = this.myLabel("Adresse", position, BOLD, CENTER);
    l.setLabelFor(boxAdressen);

    // Adresse übernehmen
    this.boxAdressen.addActionListener(e -> this.useGivenAddress());
  }

  private void addAddressField() {
    PositionCoordinates position;
    position = new PositionCoordinates();
    position.setPosY(SECOND);
    position.setHeight(SIZE1);
    position.setWidth(SIZE1);
    this.areaAdresse = this.myTextArea(position, ADDRESSFIELD_ROWS,
        ADDRESSFIELD_COLS);
    // TODO dynamische Größe für die Adressfläche?
    setSizes(this.areaAdresse, DimensionTool.ADDRESS_AREA);
    this.areaAdresse.setBackground(Color.WHITE);
    // Undurchsichtig (sollte schon so sein)
    this.areaAdresse.setOpaque(true);
    this.areaAdresse.setBorder(BorderFactory.createEtchedBorder());
  }

  private void clearAddress() {
    this.areaAdresse.setText(Constants.EMPTY);
  }

  private void saveAddress() {
    PanelControl.saveAddress(this.areaAdresse.getText());
    this.refreshBoxes();
  }

  public void refreshBoxes() {
    // Alte Adressen werden gelöscht
    this.boxAdressen.removeAllItems();

    final Company activeCompany = Session.getActiveCompany();
    final List<Address> knownAddresses = DataBase.getAddresses(activeCompany);
    for (final Address adresse : knownAddresses) {
      this.boxAdressen.addItem(adresse);
    }
  }

  private void useGivenAddress() {
    final Address address = (Address) this.boxAdressen.getSelectedItem();
    Session.setActiveAddress(address);
    final String addressText = PanelControl.useGivenAddress(address);
    this.areaAdresse.setText(addressText);
  }
}
