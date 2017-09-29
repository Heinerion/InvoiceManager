package de.heinerion.betriebe.view.panels.home;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.listener.DateListener;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.view.menu.MenuFactory;
import de.heinerion.betriebe.view.panels.ApplicationFrame;
import de.heinerion.betriebe.view.panels.CompanyListener;
import de.heinerion.betriebe.view.panels.ContentTabPane;
import de.heinerion.util.DateUtil;
import de.heinerion.util.DimensionUtil;
import de.heinerion.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ApplicationFrameImpl.java
 * heiner 27.03.2012
 */
@SuppressWarnings("serial")
class ApplicationFrameImpl implements
    ApplicationFrame, CompanyListener, DateListener {
  private JFrame frame;

  private Refreshable receiverPanel;

  private JProgressBar progressBar;
  private ContentTabPane contentTabPane;

  @Autowired
  ApplicationFrameImpl(GlassPane glassPane, ContentTabPane tabPane, ReceiverPanel receiverPanel) {
    contentTabPane = tabPane;
    this.receiverPanel = receiverPanel;
    progressBar = new JProgressBar();
    frame = new JFrame();

    frame.setGlassPane(glassPane);
    frame.setResizable(true);

    createWidgets();
    addWidgets();
    setupInteractions();

    refreshTitle();

    frame.pack();
  }

  @Override
  public JFrame getFrame() {
    return frame;
  }

  private void createWidgets() {
    initProgressBar(progressBar);
  }

  private void initProgressBar(JProgressBar progressBar) {
    progressBar.setString("Laden der Oberfläche wird vorbereitet");
    progressBar.setStringPainted(true);
    progressBar.setOpaque(false);
    progressBar.setPreferredSize(DimensionUtil.PROGRESS_BAR);
  }

  private void addWidgets() {
    frame.setLayout(new BorderLayout());
    frame.setJMenuBar(MenuFactory.createMenuBar(frame));
    frame.add(receiverPanel.getPanel(), BorderLayout.LINE_START);

    frame.add(contentTabPane.getPane(), BorderLayout.CENTER);
    frame.add(progressBar, BorderLayout.PAGE_END);
  }

  private void setupInteractions() {
    Session.addCompanyListener(this);
    Session.addDateListener(this);

    frame.addWindowListener(getShutDownAdapter());
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  private WindowAdapter getShutDownAdapter() {
    return new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        ConfigurationService.exitApplication();
      }
    };
  }

  @Override
  public JProgressBar getProgressBar() {
    return progressBar;
  }

  @Override
  public void notifyCompany() {
    refreshTitle();
    contentTabPane.refreshVorlagen();
  }

  @Override
  public void notifyDate() {
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
    receiverPanel.refresh();

    contentTabPane.refreshVorlagen();
  }

  /**
   * Passt den Fenstertitel auf den gewählten Betrieb an
   */
  private void refreshTitle() {
    List<String> token = new ArrayList<>();

    addCompanyAndNumber(token);
    addDate(token);

    if (Session.isDebugMode()) {
      addDebugMarks(token);
    }

    String title = String.join("\t", token);
    frame.setTitle(title);
  }

  private void addDebugMarks(List<String> token) {
    String debug = "##DEBUG##";
    String version = "(" + Session.getVersion() + ")";

    token.add(0, debug);

    token.add(debug);
    token.add(version);
  }

  private void addDate(List<String> token) {
    LocalDate date = Session.getDate();
    if (null != date) {
      token.add(DateUtil.format(date));
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