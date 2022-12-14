package de.heinerion.invoice.view.swing.menu.info;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.repositories.CompanyRepository;
import de.heinerion.invoice.util.PathUtilNG;
import de.heinerion.invoice.view.swing.menu.Menu;
import de.heinerion.invoice.view.swing.menu.*;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
public class InfoMenuEntry extends MenuEntry {
  private static final String NAME = Menu.translate("info");
  private final PathUtilNG pathUtil;
  private final CompanyRepository companyRepository;
  private final Session session;

  private JScrollPane spInfos;

  @Override
  protected void addWidgets(JDialog dialog) {
    dialog.setLayout(new BorderLayout());
    dialog.add(getBtnOk(), BorderLayout.PAGE_END);
    dialog.add(spInfos, BorderLayout.CENTER);
  }

  @Override
  protected void createWidgets() {
    JPanel pnlInfos = new JPanel();
    spInfos = new JScrollPane(pnlInfos);
    pnlInfos.setLayout(new GridLayout(0, 1));

    InfoTextComponent editor = new InfoTextComponent(pathUtil);
    pnlInfos.add(editor.getComponent());

    editor.fillCompanyInfo(companyRepository.findAll());
    editor.fillVersionInfo(session.getVersion());
    editor.render();
  }

  @Override
  protected void setTitle(JDialog dialog) {
    dialog.setTitle(NAME);
  }

  @Override
  protected void setupInteractions(JDialog dialog) {
    getBtnOk().addActionListener(arg0 -> getCloser().windowClosing(null));
  }

  @Override
  public String getLinkText() {
    return NAME;
  }
}
