package de.heinerion.betriebe.gui;

import javax.swing.*;
import java.awt.*;

/**
 * outsourced: 24.01.2012
 *
 * @author Heiner
 * @version 24.02.2012
 */
@SuppressWarnings("serial")
public abstract class AbstractGridPanel extends JPanel {
  private GridBagLayout gridLayout = new GridBagLayout();
  private GridBagConstraints gridConstraints = new GridBagConstraints();

  public final GridBagConstraints getGridConstraints() {
    return gridConstraints;
  }

  public final GridBagLayout getGridLayout() {
    return gridLayout;
  }

  public final void setGridConstraints(GridBagConstraints someGridConstraints) {
    this.gridConstraints = someGridConstraints;
  }

  public final void setGridLayout(GridBagLayout aGridLayout) {
    this.gridLayout = aGridLayout;
  }

  /**
   * Setzt die Preferred-, Minimum- und MaximumSize einer Komponente auf den
   * selben Wert bzw die selbe Ausdehnung
   *
   * @param komponente Die Komponente, deren Werte gesetzt werden sollen
   * @param ausdehnung Die Dimension oder Ausdehnung, die die Komponente annehmen soll
   */
  public static void setSizes(Component komponente, Dimension ausdehnung) {
    // Lieblingsgröße (pack())
    komponente.setPreferredSize(ausdehnung);
    komponente.setMinimumSize(ausdehnung);
    komponente.setMaximumSize(ausdehnung);
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
    this.gridConstraints.weightx = coordinates.getPriority();
    this.gridConstraints.weighty = coordinates.getPriority();
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
   * Legt einen beschrifteten JButton im Raster an
   *
   * @param beschriftung Der Text, der auf dem Knopf stehen soll
   * @param coordinates  Position, Größe und Priorität des Elements
   * @return Den Knopf
   * @see JButton
   */
  public final JButton myButton(String beschriftung,
                                PositionCoordinates coordinates) {

    return (JButton) this.create(new JButton(beschriftung), coordinates);
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
  public final <X> JComboBox<X> myComboBox(PositionCoordinates coordinates,
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
  public final JLabel myLabel(String labelText,
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
  public final JPanel myPanel(PositionCoordinates coordinates) {

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
  public final JTextArea myTextArea(PositionCoordinates coordinates,
                                    int zeilen, int spalten) {

    return (JTextArea) this.create(new JTextArea(zeilen, spalten), coordinates);
  }
}