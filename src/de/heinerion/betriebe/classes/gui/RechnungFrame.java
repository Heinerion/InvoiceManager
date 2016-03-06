/**
 * RechnungFrame.java
 * heiner 27.03.2012
 */
package de.heinerion.betriebe.classes.gui;

import de.heinerion.betriebe.classes.data.RechnungsListe;
import de.heinerion.betriebe.classes.gui.panels.ReceiverPanel;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.enums.Utilities;
import de.heinerion.betriebe.gui.AbstractBusyFrame;
import de.heinerion.betriebe.gui.content.ContentTabPane;
import de.heinerion.betriebe.gui.menu.MenuBar;
import de.heinerion.betriebe.listener.CompanyListener;
import de.heinerion.betriebe.listener.DateListener;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.tools.DateTools;
import de.heinerion.betriebe.tools.DimensionTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
public final class RechnungFrame extends AbstractBusyFrame implements
    CompanyListener, DateListener {
  private static final Logger LOGGER = LogManager.getLogger(RechnungFrame.class);
  private static RechnungFrame instance = null;

  private ReceiverPanel receiverPanel;

  private LocalDate rechnungsdatum = LocalDate.now();

  private final JProgressBar progBar;
  private ContentTabPane contentTabPane;

  private RechnungFrame() {
    setGlassPane(new GlassPane());
    setResizable(true);

    setLayout(new BorderLayout());
    setJMenuBar(MenuBar.getInstance());
    add(createReceiverPanel(), BorderLayout.LINE_START);

    contentTabPane = ContentTabPane.getInstance();
    add(contentTabPane, BorderLayout.CENTER);

    progBar = new JProgressBar();
    initProgBar(progBar);
    add(progBar, BorderLayout.PAGE_END);

    refreshTitle();

    pack();

    Session.addCompanyListener(this);
    Session.addDateListener(this);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        LOGGER.info("Planmäßig heruntergefahren.");
        System.exit(0);
      }
    });
  }

  public static RechnungFrame getInstance() {
    if (null == instance) {
      instance = new RechnungFrame();
      Session.setActiveFrame(instance);
    }
    return instance;
  }

  private JPanel createReceiverPanel() {
    this.receiverPanel = new ReceiverPanel(this);
    return receiverPanel;
  }

  /**
   * Fensterteile erstellen
   */
  private void initProgBar(JProgressBar progressBar) {
    progressBar.setString("Laden der Oberfläche wird vorbereitet");
    progressBar.setStringPainted(true);
    progressBar.setOpaque(false);
    progressBar.setPreferredSize(DimensionTool.PROGRESS_BAR);
  }

  /**
   * Ermittelt Rechnungsnummern durch Vergleiche
   *
   * @see {@link RechnungsListe#getMaxNumber()}
   */
  public void getNumbers() {
    DataBase.getInvoices().getMaxNumber();
  }

  public JProgressBar getProgressBar() {
    return this.progBar;
  }

  public LocalDate getRechnungsdatum() {
    return this.rechnungsdatum;
  }

  /**
   * @return [dd, mm, yyyy]
   */
  public int[] getRechnungsdatumAsArray() {
    return new int[]{this.rechnungsdatum.getDayOfMonth(),
        this.rechnungsdatum.getMonthValue(), this.rechnungsdatum.getYear(),};
  }

  @Override
  public void notifyCompany() {
    this.refreshTitle();
    contentTabPane.refreshVorlagen();
  }

  @Override
  public void notifyDate() {
    this.rechnungsdatum = Session.getDate();
    this.refreshTitle();
  }

  public void refresh() {
    this.refreshTitle();
    this.refreshBoxes();
  }

  /**
   * Comboboxen aktualisieren
   */
  private void refreshBoxes() {
    // TODO refresh Boxes ist recht aufwendig

    this.receiverPanel.refreshBoxes();

    // Vorlagen
    contentTabPane.refreshVorlagen();
  }

  /**
   * Passt den Fenstertitel auf den gewählten Betrieb an
   */
  private void refreshTitle() {
    final List<String> token = new ArrayList<>();
    String debug = "";
    String version = "";

    if (Utilities.isDebugMode()) {
      debug = "##DEBUG##";
      version = "(v" + Session.getVersion() + ")";

      token.add(debug);
    }

    final Company activeCompany = Session.getActiveCompany();
    if (activeCompany != null) {
      token.add(activeCompany.getDescriptiveName());
      token.add(Utilities.NUMMER.getText() + (activeCompany.getInvoiceNumber() + 1));
    }

    if (null != this.rechnungsdatum) {
      token.add(DateTools.format(this.rechnungsdatum));
    }

    if (Utilities.isDebugMode()) {
      token.add(debug);
      token.add(version);
    }

    final String title = String.join("\t", token);
    this.setTitle(title);
  }
}