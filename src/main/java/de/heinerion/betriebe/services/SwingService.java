package de.heinerion.betriebe.services;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.gui.ApplicationFrame;

import javax.swing.*;

public class SwingService implements ViewService {
  @Override
  public void showExceptionMessage(Exception exception, String message) {
    JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), message,
        Translator.translate("error.pdflatex"), JOptionPane.ERROR_MESSAGE);
    if (Session.isDebugMode()) {
      StringBuilder out = new StringBuilder();
      for (StackTraceElement ste : exception.getStackTrace()) {
        out.append(ste.getMethodName())
            .append(" : ")
            .append(ste.getFileName())
            .append(" (")
            .append(ste.getLineNumber())
            .append(")\n");
      }
      JOptionPane.showMessageDialog(ApplicationFrame.getInstance(),
          exception.getLocalizedMessage() + "\n" + out,
          Translator.translate("error.pdflatex"), JOptionPane.ERROR_MESSAGE);
    }
  }
}
