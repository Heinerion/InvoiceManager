package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.repositories.CompanyRepository;
import de.heinerion.invoice.view.swing.home.*;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

class CompanyChooserPanel implements Refreshable {
  private final Session session;
  private final JPanel sidePanel;
  private final CompanyRepository companyRepository;
  private JComboBox<CompanyWrapper> companies;

  CompanyChooserPanel(CompanyCreateDialog companyCreateDialog, Session session, CompanyRepository companyRepository) {
    this.session = session;
    this.sidePanel = new JPanel();
    this.companyRepository = companyRepository;
    ComponentSize.COMPANY_PANEL.applyTo(sidePanel);
    sidePanel.setOpaque(false);
    sidePanel.setLayout(new BorderLayout());
    sidePanel.add(companyCreateDialog.getButton(), BorderLayout.LINE_END);
    // spacing
    sidePanel.add(new SidePanel(), BorderLayout.PAGE_END);
    refresh();
  }

  public void refresh() {
    if (companies != null) {
      sidePanel.remove(companies);
    }
    companies = createCompanyBox();
    sidePanel.add(companies, BorderLayout.CENTER);
    notifySession();
  }

  private JComboBox<CompanyWrapper> createCompanyBox() {
    JComboBox<CompanyWrapper> companyBox = new JComboBox<>(
        companyRepository
            .findAll()
            .stream()
            .sorted(Comparator.comparing(Company::getOfficialName))
            .map(CompanyWrapper::new)
            .toArray(CompanyWrapper[]::new)
    );
    companyBox.addActionListener(e -> notifySession());
    companyBox.setOpaque(false);

    return companyBox;
  }

  private void notifySession() {
    session
        .setActiveCompany(companies
            .getItemAt(companies.getSelectedIndex())
            .getCompany());
  }

  @Override
  public JPanel getPanel() {
    return sidePanel;
  }

  @RequiredArgsConstructor
  private static class CompanyWrapper {
    private final Company company;

    public Company getCompany() {
      return company;
    }

    @Override
    public String toString() {
      return company.getOfficialName();
    }
  }
}
