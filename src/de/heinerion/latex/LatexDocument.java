package de.heinerion.latex;

import de.heinerion.betriebe.classes.file_operations.Syntax;
import de.heinerion.betriebe.data.Constants;

import java.util.*;

public class LatexDocument {
  private static final String BEGIN = "\\begin{document}";
  private static final String END = "\\end{document}";

  private static final String DELIM = ", ";
  private static final String NEWLINE = "\n";

  private final String documentType;
  private final Map<String, String> docTypeArguments;
  private final List<LatexCommand> commands;
  private final List<LatexPackage> packageList;
  private final List<HyperCommand> hypersetup;

  private boolean compressed;
  private String date;

  private String content;

  private String delimiter;

  public LatexDocument(String aDocumentType) {
    this.documentType = aDocumentType;
    this.docTypeArguments = new HashMap<>();
    this.packageList = new ArrayList<>();
    this.commands = new ArrayList<>();
    this.hypersetup = new ArrayList<>();
    this.setCompressed(false);
  }

  /**
   * Argumente für das Einrichten des {@code hyperref}-Pakets
   *
   * @param key   Name des Schlüssels
   * @param value Wert des Arguments
   */
  public final void addHyperSetupArgument(String key, String value) {
    final HyperCommand argument = new HyperCommand(key, value);
    hypersetup.add(argument);
  }

  public final void addPackage(LatexPackage aPackage) {
    this.packageList.add(aPackage);
  }

  public final void addPackage(String name) {
    this.addPackage(name, null);
  }

  public final void addPackage(String name, String arguments) {
    final LatexPackage pack = new LatexPackage(name, arguments);
    this.addPackage(pack);
  }

  public final void addTypeArgument(String key, String value) {
    this.docTypeArguments.put(key, value);
  }

  private String buildDoctype() {
    final List<String> argumentList = new ArrayList<>();
    for (String key : this.docTypeArguments.keySet()) {
      argumentList.add(key + "=" + this.docTypeArguments.get(key));
    }
    Collections.sort(argumentList);

    final String arguments = String.join(DELIM, argumentList);

    return "\\documentclass[" + arguments + "]"
        + Syntax.embrace(this.documentType);
  }

  private String buildHyperSetup() {
    final List<String> token = new ArrayList<>();

    for (HyperCommand hyperCommand : hypersetup) {
      token.add(hyperCommand.toString());
    }
    // Collections.sort(token);

    final String tokenlist = String.join(DELIM, token);

    return "\\hypersetup" + Syntax.embrace(tokenlist);
  }

  private String buildPackages() {
    final List<String> declarations = new ArrayList<>();
    for (LatexPackage aPackage : this.packageList) {
      declarations.add(aPackage.toString());
    }

    return String.join(this.delimiter, declarations);
  }

  /**
   * Wird unmittelbar nach dem Inhalt ausgegeben
   */
  protected String buildPostContent() {
    // Zum Überschreiben
    return "";
  }

  /**
   * Wird unmittelbar vor dem Inhalt ausgegeben
   */
  protected String buildPreContent() {
    // Zum Überschreiben
    return "";
  }

  /**
   * Wird nach Paketen und Befehlen vor dem Dokumentenbegin ausgegeben.
   */
  protected String buildSpecialVariables() {
    // Zum Überschreiben
    return "";
  }

  protected final String getDelimiter() {
    return delimiter;
  }

  protected final String indent(String text, String indentation) {
    String newline = Constants.NEWLINE;
    String[] lines = text.split(newline);
    String result = String.join(newline + indentation, lines);
    return indentation + result;
  }

  public final boolean isCompressed() {
    return this.compressed;
  }

  public final void renewCommand(String name, String newValue) {
    final LatexCommand command = new LatexCommand(name, newValue, true);
    this.commands.add(command);
  }

  public final void setCompressed(boolean isCompressed) {
    this.compressed = isCompressed;
    this.setDelimiter();
  }

  public final void setContent(String anyContent) {
    this.content = anyContent;
  }

  public final <T extends AbstractLatexContent> void setContent(T someContent) {
    setContent(someContent.toString());
  }

  public final <T extends AbstractLatexContent> void setDate(T dateContent) {
    this.date = dateContent.toString();
  }

  public final void setDate(String dateText) {
    this.date = dateText;
  }

  private void setDelimiter() {
    this.delimiter = this.compressed ? "" : Constants.NEWLINE;
  }

  @Override
  public final String toString() {
    final List<String> components = new ArrayList<>();
    components.add(this.buildDoctype());
    components.add(this.buildPackages());
    if (hypersetup.size() > 0) {
      components.add(this.buildHyperSetup());
    }
    for (LatexCommand command : commands) {
      components.add(command.toString());
    }
    components.add(buildSpecialVariables());
    if (this.date != null) {
      components.add("\\date{\n" + this.indent(this.date, "\t") + "\n}");
    }
    components.add(BEGIN);
    components.add(this.buildPreContent());
    if (this.content != null && !this.content.isEmpty()) {
      components.add(this.content);
    }
    components.add(this.buildPostContent());
    components.add(END);

    return String.join(this.delimiter, components);
  }
}
