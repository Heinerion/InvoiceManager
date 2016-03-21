package de.heinerion.latex;

import de.heinerion.betriebe.fileoperations.Syntax;
import de.heinerion.betriebe.data.Constants;

import java.util.*;
import java.util.stream.Collectors;

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
    List<String> argumentList = docTypeArguments.keySet().stream()
        .map(key -> key + "=" + docTypeArguments.get(key))
        .collect(Collectors.toList());

    Collections.sort(argumentList);

    String arguments = String.join(DELIM, argumentList);

    return "\\documentclass[" + arguments + "]"
        + Syntax.embrace(documentType);
  }

  private String buildHyperSetup() {
    List<String> token = hypersetup.stream()
        .map(HyperCommand::toString)
        .collect(Collectors.toList());

    String tokenList = String.join(DELIM, token);

    return "\\hypersetup" + Syntax.embrace(tokenList);
  }

  private String buildPackages() {
    List<String> declarations = packageList.stream()
        .map(LatexPackage::toString)
        .collect(Collectors.toList());

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
    if (!hypersetup.isEmpty()) {
      components.add(this.buildHyperSetup());
    }
    List<String> mappedCommands = commands.stream()
        .map(LatexCommand::toString)
        .collect(Collectors.toList());
    components.addAll(mappedCommands);
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
