package de.heinerion.invoice.view.swing.home;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.listener.*;
import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.services.ConfigurationService;
import de.heinerion.invoice.view.DateUtil;
import de.heinerion.invoice.view.swing.ApplicationFrame;
import de.heinerion.invoice.view.swing.menu.MenuFactory;
import de.heinerion.invoice.view.swing.tab.ContentTabPane;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

/**
 * ApplicationFrameImpl.java heiner 27.03.2012
 */
@Service
class ApplicationFrameImpl implements ApplicationFrame, ActiveCompanyChangedListener, DateListener {
  private final Session session;
  private final JFrame frame;

  private final Refreshable receiverPanel;

  private final ContentTabPane contentTabPane;
  private final MenuFactory menuFactory;

  ApplicationFrameImpl(GlassPane glassPane, ContentTabPane tabPane, ReceiverPanel receiverPanel, MenuFactory menuFactory, Session session) {
    this.session = session;
    this.menuFactory = menuFactory;
    contentTabPane = tabPane;
    this.receiverPanel = receiverPanel;
    frame = new JFrame();

    frame.setGlassPane(glassPane.getComponent());
    frame.setResizable(true);

    addWidgets();
    setupInteractions();

    refreshTitle();

    frame.pack();
  }

  @Override
  public JFrame getFrame() {
    return frame;
  }

  private void addWidgets() {
    frame.setLayout(new BorderLayout());
    frame.setJMenuBar(menuFactory.createMenuBar(frame));
    frame.add(receiverPanel.getPanel(), BorderLayout.LINE_START);

    frame.add(contentTabPane.getPane(), BorderLayout.CENTER);
  }

  private void setupInteractions() {
    session.addActiveCompanyListener(this);
    session.addDateListener(this);
    session.setApplicationFrame(this);

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
    // reason: the receiver panel updates the company chooser, which in turn re-selects the company which triggers another round of updates...
    receiverPanel.refresh();

    contentTabPane.refreshContents();
  }

  /**
   * Updates the window title according to the chosen company
   */
  private void refreshTitle() {
    List<String> token = new ArrayList<>();

    session.getActiveCompany()
        .map(this::getCompanyToken)
        .ifPresent(token::addAll);

    Optional.ofNullable(session.getDate())
        .map(DateUtil::format)
        .ifPresent(token::add);

    if (session.isDebugMode()) {
      addDebugMarks(token);
    }

    String title = String.join(" \t-\t ", token);
    frame.setTitle(title);
  }

  private void addDebugMarks(List<String> token) {
    String debug = "##DEBUG##";
    String version = "(%s)".formatted(session.getVersion());

    token.add(0, debug);

    token.add(debug);
    token.add(version);
  }

  private List<String> getCompanyToken(Company activeCompany) {
    return Arrays.asList(
        activeCompany.getDescriptiveName(),
        "%s: %d".formatted(
            Translator.translate("invoice.number"),
            activeCompany.getInvoiceNumber()));
  }
}