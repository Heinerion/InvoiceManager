package de.heinerion.betriebe.view.latex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

final class LatexScrLetter extends LatexDocument {
  private static final String LETTER = "letter";

  private String recipient;
  private final List<KomaVar> komavars;

  LatexScrLetter(String aRecipient) {
    super();
    setRecipient(aRecipient);
    komavars = new ArrayList<>();
  }

  private void addKomaVar(KomaVar komaVar) {
    komavars.add(komaVar);
  }

  void addKomaVar(KomaKey aKey, String aValue) {
    final KomaVar komaVar = new KomaVar(aKey, aValue);
    addKomaVar(komaVar);
  }

  private void setRecipient(String aRecipient) {
    this.recipient = aRecipient;
  }

  @Override
  protected String buildPreContent() {
    return String.join(getDelimiter(), "\\begin{" + LETTER
            + Syntax.NEXT_GROUP + recipient + Syntax.END, "",
        "\\opening{}\\vspace{-25pt}");
  }

  @Override
  protected String buildPostContent() {
    return String.join(getDelimiter(), "\\vfill",
        "\\closing{}", "", Syntax.endEnv(LETTER));
  }

  @Override
  protected String buildSpecialVariables() {
    final List<String> vars = komavars.stream()
        .map(KomaVar::toString)
        .collect(Collectors.toList());

    return String.join("\n", vars);
  }
}