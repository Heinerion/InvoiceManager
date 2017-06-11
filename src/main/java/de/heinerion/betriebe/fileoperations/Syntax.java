package de.heinerion.betriebe.fileoperations;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.tools.FormatUtil;

public final class Syntax {
  public static final String BR = "\\\\";

  public static final String EOL = Constants.NEWLINE;

  public static final String NEXT_GROUP = "}{";

  public static final String START = "{";
  public static final String END = "}";

  public static final String NEWLINE = BR + EOL;

  public static final String TEXT_TINY = "\\tiny";

  private Syntax() {
  }

  public static String begin(String environment, String options) {
    return String.format("\\begin{%s%s}", environment, (options != null) ? NEXT_GROUP + options : "");
  }

  public static String embrace(String text) {
    return String.format("{%s}", text);
  }

  public static String embrace(Object obj) {
    return embrace(obj.toString());
  }

  public static String endEnv(String environment) {
    return String.format("\\end{%s}", environment);
  }

  public static String euro(double euro) {
    return euro(FormatUtil.formatLocaleDecimal(euro));
  }

  public static String euro(String euro) {
    return String.format("\\EUR{%s}", euro);
  }

  public static String multicol(String style, String content) {
    return multicol(1, style, content);
  }

  public static String multicol(int width, String style, String content) {
    return String.format("\\multicolumn{%d}{%s}{%s}", width, style, content);
  }

  public static String sc(String text) {
    return "\\textsc " + text;
  }

  public static String tab() {
    return tab(1);
  }

  public static String tab(int number) {
    StringBuilder out = new StringBuilder();
    for (int i = 0; i < number; i++) {
      out.append("&");
    }
    return out.toString();
  }
}
