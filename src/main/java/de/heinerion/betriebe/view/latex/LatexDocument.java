package de.heinerion.betriebe.view.latex;

import de.heinerion.betriebe.util.Constants;

import java.util.*;
import java.util.stream.Collectors;

class LatexDocument {
  private static final String BEGIN = "\\begin{document}";
  private static final String END = "\\end{document}";

  private static final String DELIM = ", ";
  private static final String EMPTY = "";

  private final String documentType;
  private final Map<String, String> docTypeArguments;
  private final List<LatexPackage> packageList;
  private final List<HyperCommand> hypersetup;

  private boolean compressed;
  private String date;

  private String content;

  private String delimiter;

  LatexDocument() {
    this.documentType = "scrlttr2";
    this.docTypeArguments = new HashMap<>();
    this.packageList = new ArrayList<>();
    this.hypersetup = new ArrayList<>();
    this.setCompressed(false);
  }

  /**
   * Argumente für das Einrichten des {@code hyperref}-Pakets
   *
   * @param key   Name des Schlüssels
   * @param value Wert des Arguments
   */
  final void addHyperSetupArgument(String key, String value) {
    final HyperCommand argument = new HyperCommand(key, value);
    hypersetup.add(argument);
  }

  private void addPackage(LatexPackage aPackage) {
    this.packageList.add(aPackage);
  }

  final void addHyperrefPackage() {
    addPackage("hyperref", null);
  }

  final void addPackage(String name, String arguments) {
    final LatexPackage pack = new LatexPackage(name, arguments);
    addPackage(pack);
  }

  final void addTypeArgument(String key, String value) {
    docTypeArguments.put(key, value);
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

    return String.join(delimiter, declarations);
  }

  /**
   * Wird unmittelbar nach dem Inhalt ausgegeben
   */
  String buildPostContent() {
    // Zum Überschreiben
    return EMPTY;
  }

  /**
   * Wird unmittelbar vor dem Inhalt ausgegeben
   */
  String buildPreContent() {
    // Zum Überschreiben
    return EMPTY;
  }

  /**
   * Wird nach Paketen und Befehlen vor dem Dokumentenbegin ausgegeben.
   */
  String buildSpecialVariables() {
    // Zum Überschreiben
    return EMPTY;
  }

  final String getDelimiter() {
    return delimiter;
  }

  private String indent(String text) {
    String newline = Constants.NEWLINE;
    String[] lines = text.split(newline);
    String result = String.join(newline + "\t", lines);
    return "\t" + result;
  }

  final void setCompressed(boolean isCompressed) {
    compressed = isCompressed;
    setDelimiter();
  }

  public final void setContent(String anyContent) {
    content = anyContent;
  }

  public final <T extends LatexContent> void setContent(T someContent) {
    setContent(someContent.toString());
  }

  public final void setDate(String dateText) {
    date = dateText;
  }

  private void setDelimiter() {
    delimiter = compressed ? EMPTY : Constants.NEWLINE;
  }

  @Override
  public final String toString() {
    final List<String> components = new ArrayList<>();
    components.add(buildDoctype());
    components.add(buildPackages());
    if (!hypersetup.isEmpty()) {
      components.add(buildHyperSetup());
    }
    components.add(redifineRaggedSignature());
    components.add(buildSpecialVariables());
    if (date != null) {
      components.add("\\date{\n" + indent(date) + "\n}");
    }
    components.add(BEGIN);
    components.add(buildPreContent());
    if (content != null && !content.isEmpty()) {
      components.add(content);
    }
    components.add(buildPostContent());
    components.add(END);

    return String.join(delimiter, components);
  }

  private String redifineRaggedSignature() {
    String name = "\\raggedsignature";
    String value = "\\raggedright";
    return "\\renewcommand" + Syntax.embrace(name) + Syntax.embrace(value);
  }
}
