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

  public static String begin(String environment) {
    return begin(environment, null);
  }

  public static String begin(String environment, String options) {
    return "\\begin{" + environment
        + ((options != null) ? NEXT_GROUP + options : "") + END;
  }

  public static String embrace(String text) {
    return START + text + END;
  }

  public static String embrace(Object obj) {
    return embrace(obj.toString());
  }

  public static String end(String environment) {
    return "\\end{" + environment + END;
  }

  public static String euro(double euro) {
    return euro(FormatUtil.formatLocaleDecimal(euro));
  }

  public static String euro(String euro) {
    return "\\EUR{" + euro + END;
  }

  public static String math(String content) {
    return "\\(" + content + "\\)";
  }

  public static String mathDisplay(String content) {
    return "\\[" + content + "\\]";
  }

  public static String multicol(int width, String style, String content) {
    return "\\multicolumn{" + width + NEXT_GROUP + style + NEXT_GROUP + content
        + END;
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
