package de.heinerion.betriebe.gui.content;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.tools.strings.Strings;

@SuppressWarnings("serial")
public final class LetterTabContent extends AbstractTabContent {
  private static final String DOUBLE_SLASH = "\\\\";

  private JTextField fldBetreff;
  private JTextArea areaBrief;

  protected LetterTabContent() {
    super(Constants.LETTER);

    this.fldBetreff = new JTextField();
    fldBetreff.addCaretListener(e -> Session.setActiveConveyable(getContent()));
    this.areaBrief = new JTextArea();
    // Zeilenumbruch
    this.areaBrief.setLineWrap(true);
    // Wortweise
    this.areaBrief.setWrapStyleWord(true);
    areaBrief.addCaretListener(e -> Session.setActiveConveyable(getContent()));

    final JPanel pnlBetreff = new JPanel();
    pnlBetreff.setOpaque(false);
    pnlBetreff.setLayout(new GridLayout(0, 1));

    final JLabel lblBetreff = new JLabel("Betreff:");
    lblBetreff.setFont(lblBetreff.getFont().deriveFont(Font.BOLD));

    pnlBetreff.add(lblBetreff);
    pnlBetreff.add(this.fldBetreff);

    setLayout(new BorderLayout());
    add(pnlBetreff, BorderLayout.PAGE_START);
    add(new JScrollPane(this.areaBrief), BorderLayout.CENTER);
    add(getDelete(), BorderLayout.PAGE_END);
  }

  @Override
  protected void clear() {
    this.fldBetreff.setText("");
    this.areaBrief.setText("");
  }

  public String getBetreff() {
    return this.fldBetreff.getText();
  }

  public String getLetterText() {
    return this.areaBrief.getText();
  }

  @Override
  protected Conveyable getConveyable() {
    final Company company = Session.getActiveCompany();
    final Address receiver = Session.getActiveAddress();

    final Letter ltr = new Letter(Session.getDate(), company, receiver);
    ltr.setSubject(getBetreff());

    final String text = Strings.nToSlash(getLetterText());
    final String[] textLines = text.split(Constants.NEWLINE);
    for (final String line : textLines) {
      ltr.addMessageLine(line.replaceAll(DOUBLE_SLASH, Constants.EMPTY));
    }

    return ltr;
  }
}
