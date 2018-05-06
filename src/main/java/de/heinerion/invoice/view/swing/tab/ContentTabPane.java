package de.heinerion.invoice.view.swing.tab;

import de.heinerion.betriebe.data.Session;
import de.heinerion.invoice.view.swing.TabContent;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("serial")
public class ContentTabPane {
  private final List<TabContent> tabContents;
  private JTabbedPane pane;

  ContentTabPane(List<TabContent> someTabContents) {
    tabContents = someTabContents;

    pane = new JTabbedPane();
    for (TabContent abstractTabContent : tabContents) {
      pane.addTab(abstractTabContent.getTitle(), abstractTabContent.getPanel());
    }

    pane.addChangeListener(e -> {
      final TabContent selectedComponent = getSelectedTabContent();
      Session.setActiveConveyable(selectedComponent.getContent());
    });
  }

  public JTabbedPane getPane() {
    return pane;
  }

  private TabContent getSelectedTabContent() {
    Stream<TabContent> tabContentStream = tabContents
        .stream();

    Component selectedComponent = pane.getSelectedComponent();
    if (selectedComponent != null) {
      tabContentStream = tabContentStream.filter(content -> content.getPanel().equals(selectedComponent));
    }

    return tabContentStream
        .findFirst()
        .orElseThrow(() -> new GuiPanelException("This Tab is not registered in 'tabContents'"));
  }

  public void refreshVorlagen() {
    tabContents.forEach(TabContent::refresh);
  }
}
