/**
 * ApplicationFrame.java
 * heiner 27.03.2012
 */
package de.heinerion.betriebe.gui;

import de.heinerion.betriebe.gui.panels.ReceiverPanel;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.enums.SystemAndPathsEnum;
import de.heinerion.betriebe.gui.content.ContentTabPane;
import de.heinerion.betriebe.gui.menu.MenuBar;
import de.heinerion.betriebe.listener.CompanyListener;
import de.heinerion.betriebe.listener.DateListener;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.tools.DateUtil;
import de.heinerion.betriebe.tools.DimensionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public final class ApplicationFrame extends AbstractBusyFrame implements
    CompanyListener, DateListener {
  private static final Logger LOGGER = LogManager.getLogger(ApplicationFrame.class);
  private static ApplicationFrame instance = null;

  private ReceiverPanel receiverPanel;

  private LocalDate invoiceDate = LocalDate.now();

  private JProgressBar progressBar;
  private ContentTabPane contentTabPane;

  private ApplicationFrame() {
    setGlassPane(new GlassPane());
    setResizable(true);

    createWidgets();
    addWidgets();
    setupInteractions();

    refreshTitle();

    pack();
  }

  private void createWidgets() {
    contentTabPane = ContentTabPane.getInstance();
    progressBar = new JProgressBar();
    initProgressBar(progressBar);
  }

  private void initProgressBar(JProgressBar progressBar) {
    progressBar.setString("Laden der Oberfläche wird vorbereitet");
    progressBar.setStringPainted(true);
    progressBar.setOpaque(false);
    progressBar.setPreferredSize(DimensionUtil.PROGRESS_BAR);
  }

  private void addWidgets() {
    setLayout(new BorderLayout());
    setJMenuBar(MenuBar.getInstance());
    add(createReceiverPanel(), BorderLayout.LINE_START);

    add(contentTabPane, BorderLayout.CENTER);
    add(progressBar, BorderLayout.PAGE_END);
  }

  private JPanel createReceiverPanel() {
    receiverPanel = new ReceiverPanel();
    return receiverPanel;
  }

  private void setupInteractions() {
    Session.addCompanyListener(this);
    Session.addDateListener(this);

    addWindowListener(getShutDownAdapter());
  }

  private WindowAdapter getShutDownAdapter() {
    return new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        LOGGER.info("Planmäßig heruntergefahren.");
        System.exit(0);
      }
    };
  }

  // TODO use dependency injection instead
  public static ApplicationFrame getInstance() {
    if (null == instance) {
      instance = new ApplicationFrame();
      Session.setActiveFrame(instance);
    }
    return instance;
  }

  public JProgressBar getProgressBar() {
    return progressBar;
  }

  /**
   * @return [dd, mm, yyyy]
   */
  public int[] getRechnungsdatumAsArray() {
    return new int[]{invoiceDate.getDayOfMonth(), invoiceDate.getMonthValue(), invoiceDate.getYear()};
  }

  @Override
  public void notifyCompany() {
    refreshTitle();
    contentTabPane.refreshVorlagen();
  }

  @Override
  public void notifyDate() {
    invoiceDate = Session.getDate();
    refreshTitle();
  }

  public void refresh() {
    refreshTitle();
    refreshBoxes();
  }

  /**
   * Comboboxen aktualisieren
   */
  private void refreshBoxes() {
    // TODO refresh Boxes ist recht aufwendig
    receiverPanel.refreshBoxes();

    contentTabPane.refreshVorlagen();
  }

  /**
   * Passt den Fenstertitel auf den gewählten Betrieb an
   */
  private void refreshTitle() {
    List<String> token = new ArrayList<>();

    addCompanyAndNumber(token);
    addDate(token);

    if (SystemAndPathsEnum.isDebugMode()) {
      addDebugMarks(token);
    }

    String title = String.join("\t", token);
    setTitle(title);
  }

  private void addDebugMarks(List<String> token) {
    String debug = "##DEBUG##";
    String version = "(" + Session.getVersion() + ")";

    token.add(0, debug);

    token.add(debug);
    token.add(version);
  }

  private void addDate(List<String> token) {
    if (null != invoiceDate) {
      token.add(DateUtil.format(invoiceDate));
    }
  }

  private void addCompanyAndNumber(List<String> token) {
    final Company activeCompany = Session.getActiveCompany();
    if (activeCompany != null) {
      token.add(activeCompany.getDescriptiveName());
      String numberLabel = Translator.translate("invoice.number");
      token.add(numberLabel + ": " + (activeCompany.getInvoiceNumber() + 1));
    }
  }
}