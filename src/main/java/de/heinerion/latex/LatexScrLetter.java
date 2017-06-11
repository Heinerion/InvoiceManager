package de.heinerion.latex;

import de.heinerion.betriebe.fileoperations.Syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class LatexScrLetter extends LatexDocument {
  private static final String LETTER = "letter";

  private String recipient;
  private List<KomaVar> komavars;

  public LatexScrLetter(String aRecipient) {
    super("scrlttr2");
    setRecipient(aRecipient);
    komavars = new ArrayList<>();
  }

  public void addKomaVar(KomaVar komaVar) {
    komavars.add(komaVar);
  }

  public void addKomaVar(KomaKey aKey, String aValue) {
    final KomaVar komaVar = new KomaVar(aKey, aValue);
    addKomaVar(komaVar);
  }

  public void setRecipient(String aRecipient) {
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
