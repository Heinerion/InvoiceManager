package de.heinerion.betriebe.gui.content;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.latex.LatexGenerator;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public final class LetterTabContent extends AbstractTabContent {
  private static final String DOUBLE_SLASH = "\\\\";

  private JTextField subjectFld;
  private JPanel subjectPnl;
  private JLabel subjectLbl;
  private JTextArea contentArea;
  private JScrollPane contentScroll;

  private transient LatexGenerator latexGenerator = new LatexGenerator();

  protected LetterTabContent() {
    super(Translator.translate("letter.title"));

    createWidgets();
    addWidgets();
    setupInteraction();
  }

  private void createWidgets() {
    createSubjectPanel();
    createSubjectField();
    createContentArea();
    createContentScroller();
  }

  private void createSubjectField() {
    subjectFld = new JTextField();
  }

  private void createContentArea() {
    contentArea = new JTextArea();
    contentArea.setLineWrap(true);
    contentArea.setWrapStyleWord(true);
  }

  private void createContentScroller() {
    contentScroll = new JScrollPane(contentArea);
  }

  private void createSubjectPanel() {
    subjectPnl = new JPanel();
    subjectPnl.setOpaque(false);
    subjectPnl.setLayout(new GridLayout(0, 1));

    subjectLbl = new JLabel(Translator.translate("letter.subject") + ":");
    subjectLbl.setFont(getBoldFont());
  }

  private Font getBoldFont() {
    return subjectLbl.getFont().deriveFont(Font.BOLD);
  }

  private void addWidgets() {
    setLayout(new BorderLayout());

    add(subjectPnl, BorderLayout.PAGE_START);
    subjectPnl.add(subjectLbl);
    subjectPnl.add(subjectFld);

    add(contentScroll, BorderLayout.CENTER);
    add(getDeleteBtn(), BorderLayout.PAGE_END);
  }

  private void setupInteraction() {
    subjectFld.addCaretListener(e -> Session.setActiveConveyable(getContent()));
    contentArea.addCaretListener(e -> Session.setActiveConveyable(getContent()));
  }

  @Override
  protected void clear() {
    subjectFld.setText("");
    contentArea.setText("");
  }

  public String getBetreff() {
    return subjectFld.getText();
  }

  public String getLetterText() {
    return contentArea.getText();
  }

  @Override
  protected Letter getConveyable() {
    Company company = Session.getActiveCompany();
    Address receiver = Session.getActiveAddress();

    Letter ltr = new Letter(Session.getDate(), company, receiver);
    ltr.setSubject(getBetreff());

    String text = latexGenerator.nToSlash(getLetterText());
    String[] textLines = text.split(Constants.NEWLINE);
    for (String line : textLines) {
      ltr.addMessageLine(line.replaceAll(DOUBLE_SLASH, Constants.EMPTY));
    }

    return ltr;
  }
}