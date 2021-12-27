package de.heinerion.invoice.view.swing.home;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.listener.CompanyListener;
import de.heinerion.betriebe.listener.DateListener;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.DateUtil;
import de.heinerion.invoice.view.common.StatusComponent;
import de.heinerion.invoice.view.swing.ApplicationFrame;
import de.heinerion.invoice.view.swing.PanelFactory;
import de.heinerion.invoice.view.swing.menu.MenuFactory;
import de.heinerion.invoice.view.swing.tab.ContentTabPane;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ApplicationFrameImpl.java heiner 27.03.2012
 */
@Service
class ApplicationFrameImpl implements
    ApplicationFrame, CompanyListener, DateListener {
  private final JFrame frame;

  private final Refreshable receiverPanel;

  private final StatusComponent statusComponent;
  private final ContentTabPane contentTabPane;
  private final PathUtilNG pathUtil;
  private final MenuFactory menuFactory;

  ApplicationFrameImpl(GlassPane glassPane, ContentTabPane tabPane, ReceiverPanel receiverPanel, PathUtilNG pathUtil, MenuFactory menuFactory) {
    this.pathUtil = pathUtil;
    this.menuFactory = menuFactory;
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
    frame.setJMenuBar(menuFactory.createMenuBar(frame, pathUtil));
    frame.add(receiverPanel.getPanel(), BorderLayout.LINE_START);

    frame.add(contentTabPane.getPane(), BorderLayout.CENTER);
    frame.add(statusComponent.getContainer(), BorderLayout.PAGE_END);
  }

  private void setupInteractions() {
    Session.addCompanyListener(this);
    Session.addDateListener(this);
    Session.setApplicationFrame(this);

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
  public StatusComponent getStatusComponent() {
    return statusComponent;
  }

  @Override
  public void notifyCompany() {
    refreshTitle();
    contentTabPane.refreshContents();
  }

  @Override
  public void notifyDate() {
    refreshTitle();
  }

  @Override
  public void refresh() {
    refreshTitle();
    refreshBoxes();
  }

  /**
   * refresh combo boxes
   */
  private void refreshBoxes() {
    // TODO refresh boxes seems quite costly
    receiverPanel.refresh();

    contentTabPane.refreshContents();
  }

  /**
   * Updates the window title according to the chosen company
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
    Session.getActiveCompany()
        .ifPresent(activeCompany -> {
          token.add(activeCompany.getDescriptiveName());
          String numberLabel = Translator.translate("invoice.number");
          token.add(numberLabel + ": " + (activeCompany.getInvoiceNumber()));
        });
  }
}