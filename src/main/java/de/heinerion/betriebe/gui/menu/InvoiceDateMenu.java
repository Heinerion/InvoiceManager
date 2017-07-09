package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.gui.ApplicationFrame;
import de.heinerion.betriebe.gui.BGPanel;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.tools.DateUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
public final class InvoiceDateMenu extends AbstractMenu {
  static final String NAME = Translator.translate("menu.invoiceDate");

  private JSpinner fldYY;
  private JSpinner fldMM;
  private JSpinner fldDD;
  private JLabel lblYY;
  private JLabel lblMM;
  private JLabel lblDD;
  private JButton btnToday;
  private transient LocalDate date;

  private JLabel header;

  InvoiceDateMenu(final ApplicationFrame origin) {
    super(origin);
  }

  @Override
  protected void addWidgets() {
    setLayout(new BorderLayout(5, 5));
    add(header, BorderLayout.PAGE_START);
    add(createBtnPnl(), BorderLayout.PAGE_END);
    add(createDatePanel(), BorderLayout.CENTER);
  }

  private JPanel createBtnPnl() {
    int rows = 2;
    int cols = 1;
    JPanel btnPnl = new JPanel(new GridLayout(rows, cols));

    btnPnl.add(btnToday);
    btnPnl.add(getBtnOk());

    return btnPnl;
  }

  private JPanel createDatePanel() {
    final BGPanel pnlDatum = new BGPanel(BGPanel.LEFT, BGPanel.RIGHT,
        BGPanel.TOP, BGPanel.BOTTOM);

    int rows = 2;
    int cols = 3;
    pnlDatum.setLayout(new GridLayout(rows, cols));

    pnlDatum.add(lblDD);
    pnlDatum.add(lblMM);
    pnlDatum.add(lblYY);
    pnlDatum.add(fldDD);
    pnlDatum.add(fldMM);
    pnlDatum.add(fldYY);

    return pnlDatum;
  }

  @Override
  protected void createWidgets() {
    btnToday = new JButton(Translator.translate("date.today"));
    lblYY = new JLabel(Translator.translate("date.year"));
    lblMM = new JLabel(Translator.translate("date.month"));
    lblDD = new JLabel(Translator.translate("date.day"));

    final int[] currentDate = getOrigin().getRechnungsdatumAsArray();
    int currentDay = currentDate[0];
    int currentMonth = currentDate[1];
    int currentYear = currentDate[2];
    int stepSize = 1;

    fldDD = configureSpinner(currentDay, 1, 31, stepSize);
    fldMM = configureSpinner(currentMonth, 1, 12, 1);
    fldYY = configureSpinner(currentYear, 1990, 2050, 1);
    fldYY.setEditor(new JSpinner.NumberEditor(fldYY, "#"));

    header = new JLabel(Translator.translate("invoice.currentDate"), SwingConstants.CENTER);

  }

  private JSpinner configureSpinner(int preselected, int min, int max, int stepSize) {
    return new JSpinner(new SpinnerNumberModel(preselected, min, max, stepSize));
  }

  private boolean isValidDate() {
    boolean correct = true;

    try {
      date = DateUtil.parse(fldDD.getValue() + "." + fldMM.getValue() + "." + fldYY.getValue());
    } catch (final DateTimeParseException e) {
      correct = false;
    }

    return correct;
  }

  @Override
  protected void setTitle() {
    setTitle(NAME);
  }

  @Override
  protected void setupInteractions() {
    getBtnOk().addActionListener(
        arg0 -> {
          if (isValidDate()) {
            Session.setDate(date);
            getCloser().windowClosing(null);
          } else {
            JOptionPane.showMessageDialog(rootPane,
                Translator.translate("error.dateFormat"), Translator.translate("error.title"),
                JOptionPane.INFORMATION_MESSAGE);
          }
        });

    btnToday.addActionListener(e -> {
      final LocalDate now = LocalDate.now();
      fldDD.setValue(now.getDayOfMonth());
      fldMM.setValue(now.getMonthValue());
      fldYY.setValue(now.getYear());
    });
  }
}
