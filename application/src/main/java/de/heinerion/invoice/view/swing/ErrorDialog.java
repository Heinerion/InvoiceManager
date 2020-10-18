package de.heinerion.invoice.view.swing;

import de.heinerion.betriebe.data.Session;
import de.heinerion.invoice.view.swing.menu.BusyFrame;
import de.heinerion.invoice.view.swing.menu.Menu;
import org.apache.maven.surefire.shared.lang3.exception.ExceptionUtils;

import javax.swing.*;
import java.awt.*;

public class ErrorDialog {

  private ErrorDialog() {
    // hide public constructor
  }

  public static void show(Throwable exception) {
    // Strings will bes displayed as JLabels
    Object[] completeMessage = new Object[]{
        Menu.translate("error.text"),
        " ",
        bold("Stacktrace"),
        createStacktraceComponent(ExceptionUtils.getStackTrace(exception))};

    JOptionPane.showMessageDialog(
        /* parent component = */  Session.getApplicationFrame().getFrame(),
        /* Array of messages = */ completeMessage,
        /* title = */ Menu.translate("error.title"),
        /* type / symbol = */ JOptionPane.ERROR_MESSAGE);

    new BusyFrame(Session.getApplicationFrame().getFrame()).setBusy(false);
  }

  private static JPanel createStacktraceComponent(String stackTrace) {
    JTextArea area = new JTextArea(stackTrace);
    JScrollPane scrollPane = new JScrollPane(area);
    // padding to not show scrollbars if the content fits tightly
    int padding = 10;
    scrollPane.setPreferredSize(new Dimension(
        Math.min(area.getPreferredSize().width + padding, 1080),
        Math.min(area.getPreferredSize().height + padding, 400)));

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    panel.add(scrollPane, BorderLayout.CENTER);
    return panel;
  }

  private static JLabel bold(String text) {
    JLabel jLabel = new JLabel(text);
    jLabel.setFont(jLabel.getFont().deriveFont(Font.BOLD));
    return jLabel;
  }

  public static void catchAll(Runnable intf) {
    try {
      intf.run();
    } catch (RuntimeException e) {
      ErrorDialog.show(e);
    }
  }
}
