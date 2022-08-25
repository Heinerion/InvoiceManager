package de.heinerion.invoice.view.swing.menu.info;

import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

@Service
class InfoTextComponent {
  private final StringBuilder content;
  private final JEditorPane pane;

  InfoTextComponent() {
    content = new StringBuilder();

    pane = new JEditorPane();
    pane.setContentType("text/html; charset=UTF-8");
    pane.setEditable(false);
  }

  InfoTextComponent addHTML(String htmlMarkup) {
    content.append(htmlMarkup);
    return this;
  }

  InfoTextComponent addLine(String htmlMarkup) {
    return addHTML(htmlMarkup)
        .addNewline();
  }

  private InfoTextComponent addNewline() {
    content.append("<br />");
    return this;
  }

  InfoTextComponent addDefinitionList(Map<String, String> definitions) {
    content.append("<dl>");

    for (Map.Entry<String, String> entry : definitions.entrySet()) {
      content
          .append("<dt>")
          .append(entry.getKey())
          .append("</dt>")
          .append("<dd>")
          .append(entry.getValue())
          .append("</dd>");
    }

    content.append("</dl>");

    return this;
  }

  InfoTextComponent render() {
    pane.setText(content.toString());
    return this;
  }

  Component getComponent() {
    return pane;
  }
}
