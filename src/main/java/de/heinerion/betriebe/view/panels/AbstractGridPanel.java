package de.heinerion.betriebe.view.panels;

import javax.swing.*;
import java.awt.*;

/**
 * outsourced: 24.01.2012
 *
 * @author Heiner
 * @version 24.02.2012
 * @version 14.09.2017
 */
@SuppressWarnings("serial")
abstract class AbstractGridPanel extends JPanel {
  private final GridBagLayout gridLayout = new GridBagLayout();
  private GridBagConstraints gridConstraints = new GridBagConstraints();

  /**
   * Configures preferred, minimum and maximum size of a component at once
   *
   * @param component the component to be configured
   * @param dimension the dimension this component should have
   */
  static void setSizes(Component component, Dimension dimension) {
    component.setPreferredSize(dimension);
    component.setMinimumSize(dimension);
    component.setMaximumSize(dimension);
  }

  GridBagConstraints getGridConstraints() {
    return gridConstraints;
  }

  void setGridConstraints(GridBagConstraints someGridConstraints) {
    this.gridConstraints = someGridConstraints;
  }

  GridBagLayout getGridLayout() {
    return gridLayout;
  }

  /**
   * Legt eine neue Komponente in das Raster.<br>
   * Diese Methode wird nur intern aufgerufen und generalisiert die Erstellung
   * spezieller Komponenten
   *
   * @param komponente Die Komponente die im Raster Abgelegt werden soll
   * @return Die angelegte Komponente
   */
  private JComponent create(JComponent komponente,
                            PositionCoordinates coordinates) {
    this.gridConstraints.weightx = 0;
    this.gridConstraints.weighty = 0;
    this.gridConstraints.gridx = coordinates.getPosX();
    this.gridConstraints.gridy = coordinates.getPosY();
    this.gridConstraints.gridwidth = coordinates.getWidth();
    this.gridConstraints.gridheight = coordinates.getHeight();

    this.gridLayout.setConstraints(komponente, this.gridConstraints);
    komponente.setOpaque(false);
    komponente.setForeground(this.getForeground());
    this.add(komponente);
    return komponente;
  }

  /**
   * Erstellt eine JComboBox für Inhalte des gegebenen Typs im Raster
   *
   * @param coordinates Position, Größe und Priorität des Elements
   * @param liste       Eine Liste die den Inhaltstyp der JComboBox bestimmt
   * @return Eine JComboBox die das geforderte leistet
   * @see JComboBox
   */
  @SuppressWarnings("unchecked")
  <X> JComboBox<X> myComboBox(PositionCoordinates coordinates,
                              X[] liste) {

    return (JComboBox<X>) this.create(new JComboBox<>(liste), coordinates);
  }

  /**
   * Erstellt ein JLabel im Raster
   *
   * @param labelText    Text der auf dem Label erscheinen soll
   * @param coordinates  Position, Größe und Priorität des Elements
   * @param stil         Die Schriftanpassung (Font.ITALIC, Font.BOLD etc)
   * @param hAusrichtung Die horizontale Ausrichtung (SwingConstants: LEFT, CENTER etc)
   * @return Das angeforderte JLabel
   * @see JLabel
   */
  JLabel myLabel(String labelText,
                 PositionCoordinates coordinates, int stil, int hAusrichtung) {

    final JLabel newLabel = new JLabel(labelText);
    newLabel.setFont(this.getFont().deriveFont(stil));
    newLabel.setHorizontalAlignment(hAusrichtung);

    return (JLabel) this.create(newLabel, coordinates);
  }

  /**
   * Erstellt ein JPanel im Raster
   *
   * @param coordinates Position, Größe und Priorität des Elements
   * @return Das JPanel
   * @see JPanel
   */
  JPanel myPanel(PositionCoordinates coordinates) {

    return (JPanel) this.create(new JPanel(), coordinates);
  }

  /**
   * Erstellt eine JTextArea angegebener Größe im Raster
   *
   * @param coordinates Position, Größe und Priorität des Elements
   * @param zeilen      Anzahl der Reihen / Zeilen
   * @param spalten     Anzahl der Spalten
   * @return Die geforderte JTextArea
   * @see JTextArea
   */
  JTextArea myTextArea(PositionCoordinates coordinates,
                       int zeilen, int spalten) {
    return (JTextArea) this.create(new JTextArea(zeilen, spalten), coordinates);
  }
}