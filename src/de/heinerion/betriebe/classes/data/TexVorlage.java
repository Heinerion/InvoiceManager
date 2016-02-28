package de.heinerion.betriebe.classes.data;

import java.io.File;
import java.io.Serializable;

import de.heinerion.betriebe.classes.texting.DropListable;
import de.heinerion.betriebe.enums.Pfade;

// TODO zu RawTemplate machen, Coupling reduzieren
@SuppressWarnings("serial")
public final class TexVorlage implements DropListable, Serializable {

  // private static final int PADDING = 5;
  // private static final String INIT_STRING = "";
  // private static final String EOL = "\n";
  // /** Klammerungssymbol "##" */
  // private static final String DELIMITER = "#{2}";
  //
  // /** Generierte UID */
  // private static final long serialVersionUID = -6073798754092187955L;
  // /** Zeigt an, ob Belegung vorgenommen wurde */
  // private boolean hasValue = false;
  /** Vorlagenbezeichnung */
  private String name;

  private final String path;

  // /** Bezeichnungen der Felder */
  // private String[] variablesInText;
  // private String[] variablesOnScreen;
  // /** Map um Variablen Belegungen zuzuweisen */
  // private Map<String, String> mapValues;
  // /** Textpassagen ohne Variablen */
  // private String[] passages;
  //
  public TexVorlage(String aName) {
    this.name = aName.split("\\.", 2)[0];
    this.path = Pfade.VORLAGENSPEZIAL + File.separator + aName;
  }

  //
  // // TODO Benötige Mapping von veriablen[] auf Einfügestellen
  // // sodass doppelte Vars einen einzigen Eintrag haben
  //
  // /**
  // * Says if all values are given
  // *
  // * @return <code>true</code> if all values are given;<br>
  // * <code>false</code> otherwise
  // */
  // public boolean allValuesGiven() {
  // for (final String var : this.variablesOnScreen) {
  // if (this.mapValues.get(var).equals(INIT_STRING)) {
  // // At least one value is missing
  // return false;
  // }
  // }
  // // all values given
  // return true;
  // }

  /**
   * returns the name of this <code>Vorlage</code>
   *
   * @return Vorlagenname
   */
  @Override
  public String getName() {
    return this.name;
  }

  // /**
  // * creates a <code>JPanel</code> showing all variables in
  // <code>JLabel</code>s
  // * next to <code>JTextField</code>s to add Values to them
  // *
  // * @return JPanel with a grid of <code>JLabel</code>s and
  // * <code>JTextField</code>s
  // */
  // public JPanel getPanel() {
  // this.readVorlage(this.readFile());
  // final String[] vars = this.getVariables();
  // final String[] beleg = new String[vars.length];
  // final JPanel pnl = new JPanel(new GridLayout(0, 2, 5, 0));
  // final JPanel out = new JPanel();
  // final JLabel[] lbls = new JLabel[vars.length];
  // final JButton btn = new JButton("OK");
  // final JTextField[] flds = new JTextField[vars.length];
  //
  // for (int i = 0; i < vars.length; i++) {
  // lbls[i] = new JLabel(vars[i]);
  // flds[i] = new JTextField();
  // pnl.add(lbls[i]);
  // pnl.add(flds[i]);
  // }
  //
  // btn.addActionListener(e -> {
  // for (int i = 0; i < vars.length; i++) {
  // beleg[i] = flds[i].getText();
  // }
  // this.putValues(beleg);
  // this.hasValue = true;
  // });
  //
  // out.setLayout(new BorderLayout(PADDING, PADDING));
  // out.setBorder(BorderFactory.createEmptyBorder(0, PADDING, 0, PADDING));
  // out.add(pnl, BorderLayout.CENTER);
  // out.add(btn, BorderLayout.PAGE_END);
  //
  // pnl.setOpaque(false);
  // out.setOpaque(false);
  //
  // return out;
  // }

  public String getPath() {
    return this.path;
  }

  // public String[] getVariables() {
  // this.variablesOnScreen = this.mapValues.keySet().toArray(
  // new String[this.mapValues.keySet().size()]);
  // Arrays.sort(this.variablesOnScreen);
  // return this.variablesOnScreen;
  // }
  //
  // private void initMap() {
  // this.mapValues = new HashMap<>();
  // for (final String var : this.variablesInText) {
  // this.mapValues.put(var, INIT_STRING);
  // }
  // }
  //
  // public boolean istBelegt() {
  // return this.hasValue;
  // }
  //
  // public String out() {
  // final int var = this.variablesInText.length;
  // final int pass = this.passages.length;
  // final int min = (var > pass) ? pass : var;
  //
  // String out = "";
  //
  // int i;
  // for (i = 0; i < min; i++) {
  // out += this.passages[i] + this.mapValues.get(this.variablesInText[i]);
  // // Mehrausgabe möglich (falls Mehr Belegungen als Vars)
  // }
  // for (/* i von oben */; i < pass; i++) {
  // // Die übrigen Passagen
  // out += this.passages[i];
  // }
  // return out;
  // }
  //
  // public void putValues(String... values) {
  // for (int i = 0; i < this.variablesOnScreen.length; i++) {
  // this.mapValues.put(this.variablesOnScreen[i], values[i]);
  // }
  // }
  //
  // private String readFile() {
  // String out = "";
  // String s;
  // try {
  // final BufferedReader in = new BufferedReader(new InputStreamReader(
  // new FileInputStream(this.getPath())));
  // while (null != (s = in.readLine())) {
  // out += s + EOL;
  // }
  //
  // in.close();
  // } catch (final IOException ex) {
  // throw new RuntimeException(ex);
  // }
  // return out;
  // }
  //
  // private void readVorlage(String text) {
  // int partCount;
  // final String[] parts = text.split(DELIMITER);
  // partCount = parts.length;
  // // 50% are max possible
  // this.variablesInText = new String[partCount / 2];
  // this.passages = new String[partCount - this.variablesInText.length];
  //
  // for (int i = 0; i < parts.length; i++) {
  // if (i % 2 != 0) {
  // // every second part is a variable
  // this.variablesInText[i / 2] = parts[i];
  // } else {
  // // all others are passages
  // this.passages[i / 2] = parts[i];
  // }
  // }
  //
  // this.initMap();
  // }

  @Override
  public void setName(String aName) {
    this.name = aName;
  }

  // /**
  // * returns a String representation of the values for the specific variables
  // *
  // * @return String representation of the values or "", if not all are given
  // * @see #allValuesGiven()
  // */
  // public String values() {
  // String out = "";
  // if (this.allValuesGiven()) {
  // for (final String aVariablesInText : this.variablesInText) {
  // out += aVariablesInText + "\t -> "
  // + this.mapValues.get(aVariablesInText) + EOL;
  // }
  // }
  // return out;
  // }
}
