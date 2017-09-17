package de.heinerion.betriebe.gui.panels;

import de.heinerion.betriebe.data.Session;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
class ContentTabPaneImpl implements ContentTabPane {
  private static ContentTabPaneImpl instance;

  private JTabbedPane pane;

  private final List<TabContent> tabContents;

  private ContentTabPaneImpl() {
    pane = new JTabbedPane();

    tabContents = new ArrayList<>();

    tabContents.add(new LetterTabContent());
    tabContents.add(new InvoiceTabContent());

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
    return tabContents
        .stream()
        .filter(tabContent -> tabContent.getPanel().equals(pane.getSelectedComponent()))
        .findFirst()
        .orElseThrow(() -> new GuiPanelException("This Tab is not registered in 'tabContents'"));
  }

  static ContentTabPaneImpl getInstance() {
    if (instance == null) {
      instance = new ContentTabPaneImpl();
    }
    return instance;
  }

  @Override
  public void refreshVorlagen() {
    tabContents.forEach(TabContent::refresh);
  }
}
