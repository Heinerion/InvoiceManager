package de.heinerion.betriebe.view.swing.menu;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.util.Translator;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class InfoMenu extends AbstractMenu {
  private static final String NAME = translate("menu.info");

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

    InfoTextComponent editor = new InfoTextComponent();
    pnlInfos.add(editor.getComponent());

    fillInfoComponent(editor);
    editor.render();
  }

  private void fillInfoComponent(InfoTextComponent editor) {
    editor.addHTML(bold(translate("info.available.companies")));

    Map<String, String> compInfos = new HashMap<>();
    for (Company company: Session.getAvailableCompanies()) {
      String valueMarkup = new StringBuilder()
          .append("<p>")
          .append(format("info.company.invoice.number", company.getInvoiceNumber()))
          .append("<br />")
          .append(format("info.company.vat", company.getValueAddedTax()))
          .append("<br />")
          .append(company.getFolderFile().getAbsolutePath())
          .append("</p>")
          .toString();
      compInfos.put(company.toString(), valueMarkup);
    }
    editor.addDefinitionList(compInfos);

    editor
        .addLine(bold(translate("info.version")))
        .addLine(Session.getVersion());
  }

  private String bold(String content) {
    return "<strong>"+content+"</strong>";
  }

  private static String format(String key, Object... replacements) {
    return MessageFormat.format(translate(key), replacements);
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
