package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Account;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.storage.loading.IO;
import de.heinerion.invoice.storage.xml.jaxb.Migrator;
import de.heinerion.invoice.view.swing.ApplicationFrame;
import de.heinerion.invoice.view.swing.home.receiver.forms.AccountForm;
import de.heinerion.invoice.view.swing.home.receiver.forms.AddressForm;
import de.heinerion.invoice.view.swing.home.receiver.forms.CompanyForm;
import de.heinerion.invoice.view.swing.menu.BusyFrame;
import de.heinerion.invoice.view.swing.menu.Menu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CompanyCreateDialog {
  private static final Logger logger = LogManager.getLogger(CompanyCreateDialog.class);

  private ApplicationFrame applicationFrame;
  private BusyFrame originFrame;
  /**
   * window adapter, responsible for closing
   */
  private final DisposeAdapter closer = new DisposeAdapter();
  private JDialog dialog;

  private final JButton button;

  private JButton btnSave;
  private JButton btnCancel;
  private JPanel addressFormPanel;
  private JPanel accountFormPanel;
  private JPanel companyFormPanel;
  private CompanyForm companyForm;
  private AddressForm addressForm;
  private AccountForm accountForm;

  public CompanyCreateDialog() {
    button = new JButton("+");
    button.addActionListener(e -> {
      Container contentPane = button.getRootPane().getParent();
      if (contentPane instanceof JFrame) {
        applicationFrame = Session.getApplicationFrame();
        originFrame = new BusyFrame(applicationFrame.getFrame());
        showDialog();
      } else {
        throw new RuntimeException("Could not create dialog to add new companies");
      }
    });
  }

  /**
   * Return a provided button to open this dialog
   *
   * @return {@link JButton} to open this Dialog
   */
  public JButton getButton() {
    return button;
  }

  /**
   * shows an always on top modal menu and sets the origin frame busy.
   */
  private void showDialog() {
    originFrame.setBusy(true);

    dialog = new JDialog(originFrame.getFrame(), true);
    showDialog(dialog);
  }

  private void showDialog(JDialog modalDialog) {
    modalDialog.setAlwaysOnTop(true);
    modalDialog.addWindowListener(closer);
    createWidgets();
    addWidgets(modalDialog);
    setupInteractions(modalDialog);
    modalDialog.setTitle(Menu.translate("companies.create"));
    modalDialog.pack();

    modalDialog.setLocationRelativeTo(originFrame.getFrame());
    modalDialog.setVisible(true);
  }

  private void createWidgets() {
    // Address
    addressForm = new AddressForm();
    addressFormPanel = addressForm.getPanel();

    // Account
    accountForm = new AccountForm();
    accountFormPanel = accountForm.getPanel();

    // Company
    companyForm = new CompanyForm(addressForm, accountForm);
    companyFormPanel = companyForm.getPanel();

    btnSave = new JButton(Translator.translate("controls.save"));
    btnCancel = new JButton(Translator.translate("controls.cancel"));
  }

  private void addWidgets(JDialog dialog) {
    dialog.setLayout(new BorderLayout(5, 5));
    dialog.add(companyFormPanel, BorderLayout.PAGE_START);
    dialog.add(addressFormPanel, BorderLayout.LINE_START);
    dialog.add(accountFormPanel, BorderLayout.LINE_END);

    JPanel buttons = new JPanel(new BorderLayout());
    buttons.add(btnCancel, BorderLayout.LINE_START);
    buttons.add(btnSave, BorderLayout.LINE_END);
    dialog.add(buttons, BorderLayout.PAGE_END);
  }

  private void setupInteractions(JDialog dialog) {
    btnSave.addActionListener(e -> {
      saveCompany(dialog);
    });

    btnCancel.addActionListener(e -> closeDialog(dialog));
  }

  private void saveCompany(JDialog dialog) {
    Company company = companyForm.getValue();
    if (company != null) {
      Address address = addressForm.getValue();
      company.setAddress(address);
      Account account = accountForm.getValue();
      company.setAccount(account);
      if (address != null && account != null) {
        DataBase dataBase = DataBase.getInstance();
        dataBase.addCompany(company);
        logger.info(String.format("added %s to database%n", company.getDescriptiveName()));
        new IO(new PathUtilNG()).saveCompanies(Session.getAvailableCompanies());
        logger.info(String.format("%s written to disk%n", company.getDescriptiveName()));
        Migrator.migrateCompanies(new PathUtilNG(), dataBase);
        applicationFrame.refresh();
        closeDialog(dialog);
      }
    }
  }

  private void closeDialog(JDialog dialog) {
    dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
  }

  protected final class DisposeAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      dialog.dispose();
      originFrame.setBusy(false);
    }
  }
}
