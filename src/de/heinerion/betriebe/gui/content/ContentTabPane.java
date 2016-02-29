package de.heinerion.betriebe.gui.content;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import de.heinerion.betriebe.data.Session;

@SuppressWarnings("serial")
public final class ContentTabPane extends JTabbedPane {
  private static ContentTabPane instance;

  private List<TabContent> tabContents;

  private ContentTabPane() {
    tabContents = new ArrayList<>();

    tabContents.add(new LetterTabContent());
    tabContents.add(new InvoiceTabContent());

    for (TabContent abstractTabContent : tabContents) {
      addTab(abstractTabContent.getTitle(), (JComponent) abstractTabContent);
    }

    addChangeListener(e -> {
      final TabContent selectedComponent = (TabContent) getSelectedComponent();
      Session.setActiveConveyable(selectedComponent.getContent());
    });
  }

  public static ContentTabPane getInstance() {
    if (instance == null) {
      instance = new ContentTabPane();
    }
    return instance;
  }

  public void refreshVorlagen() {
    for (TabContent abstractTabContent : tabContents) {
      abstractTabContent.refresh();
    }
  }
}