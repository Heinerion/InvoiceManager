package de.heinerion.invoice.view.swing.menu.info;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.menu.AbstractMenuEntry;
import de.heinerion.invoice.view.swing.menu.Menu;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class InfoMenuEntry extends AbstractMenuEntry {
  private static final String NAME = Menu.translate("info");
  private final PathUtilNG pathUtil;

  private JScrollPane spInfos;

  public InfoMenuEntry (PathUtilNG pathUtil) {
    this.pathUtil = pathUtil;
  }

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

    InfoTextComponent editor = new InfoTextComponent();
    pnlInfos.add(editor.getComponent());

    fillInfoComponent(editor);
    editor.render();
  }

  private void fillInfoComponent(InfoTextComponent editor) {
    editor.addHTML(bold(Info.translate("available.companies")));

    Map<String, String> compInfos = new HashMap<>();
    for (Company company: Session.getAvailableCompanies()) {
      String template = "<p>%s<br />%s<br />%s</p>";
      String valueMarkup = String.format(template,
          format("company.invoice.number", company.getInvoiceNumber()),
          format("company.vat", company.getValueAddedTax()),
          company.getFolderFile(pathUtil.determinePath(Invoice.class)).getAbsolutePath());
      compInfos.put(company.toString(), valueMarkup);
    }
    editor.addDefinitionList(compInfos);

    editor
        .addLine(bold(Info.translate("version")))
        .addLine(Session.getVersion());
  }

  private String bold(String content) {
    return "<strong>"+content+"</strong>";
  }

  private static String format(String key, Object... replacements) {
    return MessageFormat.format(Info.translate(key), replacements);
  }

  private static String translate(String key) {
    return Translator.translate(key);
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
