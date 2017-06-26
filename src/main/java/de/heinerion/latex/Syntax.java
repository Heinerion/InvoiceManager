package de.heinerion.latex;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.tools.FormatUtil;

public final class Syntax {
  static final String BR = "\\\\";

  static final String EOL = Constants.NEWLINE;

  static final String NEXT_GROUP = "}{";

  static final String START = "{";
  static final String END = "}";

  public static final String NEWLINE = BR + EOL;

  static final String TEXT_TINY = "\\tiny";

  private Syntax() {
  }

  static String begin(String environment, String options) {
    return String.format("\\begin{%s%s}", environment, (options != null) ? NEXT_GROUP + options : "");
  }

  static String embrace(String text) {
    return String.format("{%s}", text);
  }

  static String embrace(Object obj) {
    return embrace(obj.toString());
  }

  static String endEnv(String environment) {
    return String.format("\\end{%s}", environment);
  }

  static String euro(double euro) {
    return euro(FormatUtil.formatLocaleDecimal(euro));
  }

  private static String euro(String euro) {
    return String.format("\\EUR{%s}", euro);
  }

  static String multicol(String style, String content) {
    return multicol(1, style, content);
  }

  static String multicol(int width, String style, String content) {
    return String.format("\\multicolumn{%d}{%s}{%s}", width, style, content);
  }

  static String sc(String text) {
    return "\\textsc " + text;
  }

  static String tab() {
    return tab(1);
  }

  static String tab(int number) {
    StringBuilder out = new StringBuilder();
    for (int i = 0; i < number; i++) {
      out.append("&");
    }
    return out.toString();
  }
}
