package de.heinerion.invoice.view.swing.menu.info;

import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class InfoTextComponent {
  private final StringBuilder content = new StringBuilder();
  private final JEditorPane pane = createHtmlPane();
  private final PathUtilNG pathUtil;

  private static JEditorPane createHtmlPane() {
    JEditorPane editorPane = new JEditorPane();
    editorPane.setContentType("text/html; charset=UTF-8");
    editorPane.setEditable(false);
    return editorPane;
  }

  void addHTML(String htmlMarkup) {
    content.append(htmlMarkup);
  }

  void addLine(String htmlMarkup) {
    addHTML(htmlMarkup);
    addNewline();
  }

  private void addNewline() {
    content.append("<br />");
  }

  void addDefinitionList(Map<String, String> definitions) {
    content.append(definitions
        .entrySet()
        .stream()
        .map(entry -> "<dt>%s</dt><dd>%s</dd>".formatted(entry.getKey(), entry.getValue()))
        .collect(Collectors.joining("", "<dl>", "</dl>")));
  }

  void render() {
    pane.setText(content.toString());
  }

  Component getComponent() {
    return pane;
  }

  public void fillCompanyInfo(Collection<Company> companies) {
    addHTML(bold(Info.translate("available.companies")));
    addDefinitionList(companies
        .stream()
        .collect(Collectors.toMap(
            Company::toString,
            this::createCompanyMarkup,
            (a, b) -> b)));
  }

  private String createCompanyMarkup(Company company) {
    return "<p>%s<br />%s<br />%s</p>".formatted(
        Info.translate("company.invoice.number", company.getInvoiceNumber()),
        Info.translate("company.vat", company.getValueAddedTax()),
        pathUtil.determineInvoicePath(company).toAbsolutePath());
  }

  public void fillVersionInfo(String version) {
    addLine(bold(Info.translate("version")));
    addLine(version);
  }

  private String bold(String content) {
    return "<strong>%s</strong>".formatted(content);
  }
}
