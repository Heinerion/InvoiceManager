package de.heinerion.invoice.view.swing;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.view.swing.menu.Menu;
import de.heinerion.invoice.view.swing.menu.*;
import lombok.extern.flogger.Flogger;

import javax.swing.*;
import java.awt.*;
import java.io.*;

@Flogger
public class ErrorDialog {
  private final Session session;

  public ErrorDialog(Session session) {
    this.session = session;
  }

  public void show(Throwable exception) {
    log.atSevere().withCause(exception).log();

    // Strings will bes displayed as JLabels
    Object[] completeMessage = new Object[]{
        Menu.translate("error.text"),
        " ",
        bold("Stacktrace"),
        createStacktraceComponent(getStackTrace(exception))};

    JFrame activeFrame = session.getApplicationFrame().getFrame();

    JOptionPane.showMessageDialog(
        /* parent component = */  activeFrame,
        /* Array of messages = */ completeMessage,
        /* title = */ Menu.translate("error.title"),
        /* type / symbol = */ JOptionPane.ERROR_MESSAGE);

    new BusyFrame(activeFrame).setBusy(false);
  }

  private static String getStackTrace(Throwable e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    return sw.toString();
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

  public void catchAll(Runnable intf) {
    try {
      intf.run();
    } catch (RuntimeException e) {
      show(e);
    }
  }
}
