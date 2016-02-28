package de.heinerion.latex;

import de.heinerion.betriebe.classes.fileOperations.Syntax;
import de.heinerion.betriebe.data.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class LatexTable extends AbstractLatexContent {
  private static final String TABULAR = "tabular";

  private static final String COL_SEP = "|";
  private static final String LEFT = "l";
  private static final String LINE = "\\hline";

  private static final String PHANTOM = "$\\phantom{iwas}$";

  private static final boolean FILL_START = false;
  private static final boolean FILL_END = true;

  private List<Header> columnHeaders;
  private String content;
  private String columnAlignment;

  public LatexTable(String someColumns) {
    this.columnAlignment = someColumns;
    columnHeaders = new ArrayList<>();
  }

  public void add(String someContent) {
    if (this.content == null) {
      setContent(someContent);
    } else {
      this.content += someContent;
    }
  }

  public <T extends AbstractLatexContent> void add(T someContent) {
    add(someContent.toString());
  }

  public void addColumnHeader(int aWidth, String style, String aColumnHeader) {
    this.columnHeaders.add(new Header(aWidth, style, aColumnHeader));
  }

  public void addEmptyRow() {
    final int columnsCount = countColumns();

    add(Syntax.multicol(1, COL_SEP + LEFT, PHANTOM)
        + Syntax.tab(columnsCount - 1) + Syntax.NEWLINE);
  }

  /**
   * Fügt eine waagerechte Linie über die komplette Breite der Tabelle hinzu.
   */
  public void addLine() {
    add(LINE + Constants.NEWLINE);
  }

  /**
   * Fügt eine waagerechte Linie hinzu, die von der angegebenen Spalte bis zum
   * Ende der aktuellen Reihe reicht.
   * <p>
   * Ist der Index negativ, wird vom Ende gezählt.
   * 
   * @param startColumn
   *          Spaltenindex, von dem aus unterstrichen wird
   */
  public void addLine(int startColumn) {
    final int max = countColumns();

    if (startColumn > 0) {
      addLine(startColumn, max);
    } else {
      addLine(max + startColumn, max);
    }
  }

  /**
   * Fügt eine waagerechte Linie hinzu, die von der angegebenen Startspalte bis
   * zur angegebenen Endspalte der aktuellen Reihe reicht.
   * 
   * @param start
   *          Startspaltenindex
   * @param end
   *          Endspaltenindex
   */
  public void addLine(int start, int end) {
    add("\\cline{" + start + "-" + end + Syntax.END + Constants.NEWLINE);
  }

  public void addRow(String someContent) {
    add(someContent + Syntax.NEWLINE);
  }

  public <T extends AbstractLatexContent> void addRow(T someContent) {
    addRow(someContent.toString());
  }

  public void fillEnd(String... columns) {
    fill(FILL_END, columns);
  }

  public void fillMid(String start, String end) {
    fill(start, end);
  }

  public void fillStart(String... columns) {
    fill(FILL_START, columns);
  }

  private int countColumns() {
    int result = 0;
    for (Header header : columnHeaders) {
      result += header.width;
    }
    return result;
  }

  public void finishRow() {
    add(Syntax.NEWLINE);
  }

  private void fill(boolean fillEnd, String... columns) {
    final int max = countColumns();
    final int skip = max - columns.length;

    final String skippedColumns = Syntax.tab(skip);
    final String filledColumns = String.join(Syntax.tab(), columns);

    if (fillEnd) {
      add(filledColumns + skippedColumns);
    } else {
      add(skippedColumns + filledColumns);
    }
  }

  private void fill(String start, String end) {
    final int max = countColumns() - 1;
    final int skip = max - 2;

    final String skippedColumns = Syntax.tab(skip);
    final String filledRow = String.join(Syntax.tab(), start, skippedColumns,
        end);
    add(filledRow);
  }

  /**
   * @return
   */
  private String generateHeader() {
    String tableHead = "";
    tableHead += LINE;
    tableHead += Constants.NEWLINE;

    final String headers = columnHeaders.stream().map(Object::toString)
        .collect(Collectors.joining(Syntax.tab()));
    tableHead += headers;

    tableHead += Syntax.NEWLINE;
    tableHead += LINE;

    return tableHead;
  }

  public void setColumnHeaders(List<Header> someColumnHeaders) {
    this.columnHeaders = someColumnHeaders;
  }

  public void setContent(String someContent) {
    this.content = someContent;
  }

  @Override
  public String toString() {
    final List<String> elements = new ArrayList<>();
    elements.add(Syntax.begin(TABULAR, columnAlignment));

    if (columnHeaders.size() > 0) {
      elements.add(generateHeader());
    }

    elements.add(indent(this.content));
    elements.add(Syntax.end(TABULAR));

    return String.join(Constants.NEWLINE, elements);
  }

  private class Header {
    private int width;
    private String text;
    private String style = "";

    public Header(int aWidth, String aStyle, String someText) {
      this.width = aWidth;
      this.text = someText;
      this.style = aStyle;
    }

    @Override
    public String toString() {
      return Syntax.multicol(width, style, text);
    }
  }
}
