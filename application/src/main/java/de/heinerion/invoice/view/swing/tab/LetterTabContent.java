package de.heinerion.invoice.view.swing.tab;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.TabContent;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import java.awt.*;

import static java.awt.BorderLayout.*;

@Service
class LetterTabContent extends TabContent {
  private JTextField subjectFld;
  private JPanel subjectPnl;
  private JLabel subjectLbl;
  private JTextArea contentArea;
  private JScrollPane contentScroll;

  LetterTabContent() {
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
    JPanel panel = getPanel();
    panel.setLayout(new BorderLayout());

    panel.add(subjectPnl, PAGE_START);
    subjectPnl.add(subjectLbl);
    subjectPnl.add(subjectFld);

    panel.add(contentScroll, CENTER);
    panel.add(getDeleteBtn(), PAGE_END);
  }

  private void setupInteraction() {
    subjectFld.addCaretListener(this::setConveyableOnSession);
    contentArea.addCaretListener(this::setConveyableOnSession);
  }

  private void setConveyableOnSession(CaretEvent e) {
    Session.setActiveConveyable(getContent());
  }

  @Override
  protected void clear() {
    subjectFld.setText("");
    contentArea.setText("");
  }

  private String getBetreff() {
    return subjectFld.getText();
  }

  private String getLetterText() {
    return contentArea.getText();
  }

  @Override
  protected Letter getConveyable() {
    Company company = Session.getActiveCompany().orElse(null);
    if (company == null) {
      return null;
    }

    Address receiver = Session.getActiveAddress();

    Letter ltr = new Letter(Session.getDate(), company, receiver);
    ltr.setSubject(getBetreff());

    for (String line : getLetterText().split("\n")) {
      ltr.addMessageLine(line);
    }

    return ltr;
  }
}
