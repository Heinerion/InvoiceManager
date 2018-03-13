package de.heinerion.invoice.view.swing.home;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.listener.DateListener;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.view.common.StatusComponent;
import de.heinerion.invoice.view.swing.ApplicationFrame;
import de.heinerion.invoice.view.swing.CompanyListener;
import de.heinerion.invoice.view.swing.ContentTabPane;
import de.heinerion.invoice.view.swing.PanelFactory;
import de.heinerion.invoice.view.swing.menu.MenuFactory;
import de.heinerion.invoice.view.DateUtil;
import de.heinerion.invoice.Translator;
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

  private StatusComponent<JPanel> statusComponent;
  private ContentTabPane contentTabPane;
  private PathUtilNG pathUtil;

  @Autowired
  ApplicationFrameImpl(GlassPane glassPane, ContentTabPane tabPane, ReceiverPanel receiverPanel, PathUtilNG pathUtil) {
    this.pathUtil = pathUtil;
    contentTabPane = tabPane;
    this.receiverPanel = receiverPanel;
    statusComponent = PanelFactory.createStatusComponent();
    frame = new JFrame();

    frame.setGlassPane(glassPane.getComponent());
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
    initProgressBar(statusComponent);
  }

  private void initProgressBar(StatusComponent progressBar) {
    progressBar.initProgress();
    progressBar.setMessage(Translator.translate("progress.start"));
    progressBar.setSize(DimensionUtil.PROGRESS_BAR);
  }

  private void addWidgets() {
    frame.setLayout(new BorderLayout());
    frame.setJMenuBar(MenuFactory.createMenuBar(frame, pathUtil));
    frame.add(receiverPanel.getPanel(), BorderLayout.LINE_START);

    frame.add(contentTabPane.getPane(), BorderLayout.CENTER);
    frame.add(statusComponent.getContainer(), BorderLayout.PAGE_END);
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
  public StatusComponent<JPanel> getStatusComponent() {
    return statusComponent;
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