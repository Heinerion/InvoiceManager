package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.CompanyRepository;
import de.heinerion.invoice.view.swing.ApplicationFrame;
import de.heinerion.invoice.view.swing.home.receiver.forms.AddressForm;
import de.heinerion.invoice.view.swing.home.receiver.forms.*;
import de.heinerion.invoice.view.swing.menu.Menu;
import de.heinerion.invoice.view.swing.menu.*;
import lombok.extern.flogger.Flogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@Flogger
public class CompanyCreateDialog {
  private final Session session;
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

  private final CompanyRepository companyRepository;

  public CompanyCreateDialog(Session session, CompanyRepository companyRepository) {
    this.session = session;
    this.companyRepository = companyRepository;

    button = new JButton("+");
    button.addActionListener(e -> {
      Container contentPane = button.getRootPane().getParent();
      if (contentPane instanceof JFrame) {
        applicationFrame = session.getApplicationFrame();
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

    dialog = new JDialog(originFrame.frame(), true);
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

    modalDialog.setLocationRelativeTo(originFrame.frame());
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
    btnSave.addActionListener(e -> saveCompany(dialog));
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
        companyRepository.save(company);
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
