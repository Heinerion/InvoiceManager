package de.heinerion.betriebe.classes.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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
   * @param komponente
   *          Die Komponente, deren Werte gesetzt werden sollen
   * @param ausdehnung
   *          Die Dimension oder Ausdehnung, die die Komponente annehmen soll
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
   * @param komponente
   *          Die Komponente die im Raster Abgelegt werden soll
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
   * @param beschriftung
   *          Der Text, der auf dem Knopf stehen soll
   * @param coordinates
   *          Position, Größe und Priorität des Elements
   * @see JButton
   * @return Den Knopf
   */
  public final JButton myButton(String beschriftung,
      PositionCoordinates coordinates) {

    return (JButton) this.create(new JButton(beschriftung), coordinates);
  }

  /**
   * Erstellt eine JComboBox für Inhalte des gegebenen Typs im Raster
   *
   * @param coordinates
   *          Position, Größe und Priorität des Elements
   * @param liste
   *          Eine Liste die den Inhaltstyp der JComboBox bestimmt
   * @see JComboBox
   * @return Eine JComboBox die das geforderte leistet
   */
  @SuppressWarnings("unchecked")
  public final <X> JComboBox<X> myComboBox(PositionCoordinates coordinates,
      X[] liste) {

    return (JComboBox<X>) this.create(new JComboBox<>(liste), coordinates);
  }

  /**
   * Erstellt ein JLabel im Raster
   *
   * @param labelText
   *          Text der auf dem Label erscheinen soll
   * @param coordinates
   *          Position, Größe und Priorität des Elements
   * @param stil
   *          Die Schriftanpassung (Font.ITALIC, Font.BOLD etc)
   * @param hAusrichtung
   *          Die horizontale Ausrichtung (SwingConstants: LEFT, CENTER etc)
   * @see JLabel
   * @return Das angeforderte JLabel
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
   * @param coordinates
   *          Position, Größe und Priorität des Elements
   * @see JPanel
   * @return Das JPanel
   */
  public final JPanel myPanel(PositionCoordinates coordinates) {

    return (JPanel) this.create(new JPanel(), coordinates);
  }

  /**
   * Erstellt eine JTextArea angegebener Größe im Raster
   *
   * @param coordinates
   *          Position, Größe und Priorität des Elements
   * @param zeilen
   *          Anzahl der Reihen / Zeilen
   * @param spalten
   *          Anzahl der Spalten
   * @see JTextArea
   * @return Die geforderte JTextArea
   */
  public final JTextArea myTextArea(PositionCoordinates coordinates,
      int zeilen, int spalten) {

    return (JTextArea) this.create(new JTextArea(zeilen, spalten), coordinates);
  }
}