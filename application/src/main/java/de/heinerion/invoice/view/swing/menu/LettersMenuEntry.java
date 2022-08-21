package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.repositories.LetterRepository;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.print.PrintOperations;
import de.heinerion.invoice.view.swing.menu.tablemodels.NiceTable;
import de.heinerion.invoice.view.swing.menu.tablemodels.letters.LetterTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * @author heiner
 */
@Flogger
@RequiredArgsConstructor
class LettersMenuEntry extends MenuEntry {
  private static final String NAME = Menu.translate("letters");
  private final LetterRepository letterRepository;
  private final PrintOperations printOperations;

  private JButton btnPrint;

  private NiceTable<Letter> tblLetters;
  private JScrollPane spLetters;

  public static final int INDEX_SUBJECT = 0;
  public static final int INDEX_RECEIVER = 1;
  public static final int INDEX_DATE = 2;

  @Override
  protected void addWidgets(JDialog dialog) {
    dialog.setLayout(new BorderLayout());
    dialog.add(getButtonPanel(), BorderLayout.PAGE_END);
    dialog.add(spLetters, BorderLayout.CENTER);
  }

  private JPanel getButtonPanel() {
    JPanel container = new JPanel(new BorderLayout());
    container.add(btnPrint, BorderLayout.LINE_START);
    container.add(getBtnOk(), BorderLayout.LINE_END);
    return container;
  }

  @Override
  protected void createWidgets() {
    btnPrint = new JButton(Translator.translate("controls.print"));
    btnPrint.setEnabled(false);

    LetterTable model = new LetterTable(letterRepository.findAllBySender(Session.getActiveCompany().orElse(null)));
    tblLetters = new NiceTable<>(model);
    setColumnWidths(tblLetters.getColumnModel());
    tblLetters.sortBy(INDEX_DATE, SortOrder.ASCENDING);
    tblLetters.addSelectionListener(this::refreshPrintButtonState);

    spLetters = new JScrollPane(tblLetters.asJTable());
    spLetters.setPreferredSize(getBusyFrame().getSize());
  }

  private void refreshPrintButtonState(ListSelectionModel lsm) {
    btnPrint.setEnabled(!lsm.isSelectionEmpty());
  }

  private void setColumnWidths(TableColumnModel cols) {
    cols.getColumn(INDEX_SUBJECT).setPreferredWidth(200);
    cols.getColumn(INDEX_RECEIVER).setPreferredWidth(90);
    cols.getColumn(INDEX_DATE).setMinWidth(90);
    cols.getColumn(INDEX_DATE).setMaxWidth(90);
  }

  @Override
  protected void setTitle(JDialog dialog) {
    dialog.setTitle(NAME);
  }

  @Override
  public String getLinkText() {
    return NAME;
  }

  @Override
  protected void setupInteractions(JDialog dialog) {
    getBtnOk().addActionListener(ignored -> getCloser().windowClosing(null));
    btnPrint.addActionListener(ignored -> printSelectedLetter());
  }

  private void printSelectedLetter() {
    tblLetters.getSelectedRowObject().ifPresent(printOperations::createDocument);
  }
}
