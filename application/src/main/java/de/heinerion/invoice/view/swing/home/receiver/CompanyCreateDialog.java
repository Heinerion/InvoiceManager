package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.home.receiver.forms.AccountForm;
import de.heinerion.invoice.view.swing.home.receiver.forms.AddressForm;
import de.heinerion.invoice.view.swing.home.receiver.forms.CompanyForm;
import de.heinerion.invoice.view.swing.menu.BusyFrame;
import de.heinerion.invoice.view.swing.menu.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CompanyCreateDialog {
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

  public CompanyCreateDialog() {
    button = new JButton("+");
    button.addActionListener(e -> {
      Container contentPane = button.getRootPane().getParent();
      if (contentPane instanceof JFrame) {
        originFrame = new BusyFrame((JFrame) contentPane);
        showDialog();
      } else {
        System.out.println(":(");
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
    AddressForm addressForm = new AddressForm();
    addressFormPanel = addressForm.getPanel();

    // Account
    AccountForm accountForm = new AccountForm();
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
      Company company = companyForm.getValue();
      System.out.println(company);
    });

    btnCancel.addActionListener(e -> dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING)));
  }

  protected final class DisposeAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      dialog.dispose();
      originFrame.setBusy(false);
    }
  }
}
