package de.heinerion.betriebe.view.panels.tab;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.view.panels.ContentTabPane;
import de.heinerion.betriebe.view.panels.TabContent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("serial")
class ContentTabPaneImpl implements ContentTabPane {
  private final List<TabContent> tabContents;
  private JTabbedPane pane;

  @Autowired
  ContentTabPaneImpl(List<TabContent> someTabContents) {
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

  @Override
  public JTabbedPane getPane() {
    return pane;
  }

  private TabContent getSelectedTabContent() {
    Stream<TabContent> tabContentStream = tabContents
        .stream();

    if (pane.getSelectedComponent() != null) {
      tabContentStream = tabContentStream.filter(tabContent -> tabContent.getPanel().equals(pane.getSelectedComponent()));
    }

    return tabContentStream
        .findFirst()
        .orElseThrow(() -> new GuiPanelException("This Tab is not registered in 'tabContents'"));
  }

  @Override
  public void refreshVorlagen() {
    tabContents.forEach(TabContent::refresh);
  }
}
