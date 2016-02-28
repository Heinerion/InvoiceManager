package de.heinerion.latex;

import java.util.ArrayList;
import java.util.List;

import de.heinerion.betriebe.classes.fileOperations.Syntax;

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
    final String text = String.join(getDelimiter(), "\\begin{" + LETTER
        + Syntax.NEXT_GROUP + recipient + Syntax.END, "",
        "\\opening{}\\vspace{-25pt}");
    return text;
  }

  @Override
  protected String buildPostContent() {
    final String endText = String.join(getDelimiter(), "\\vfill",
        "\\closing{}", "", Syntax.end(LETTER));
    return endText;
  }

  @Override
  protected String buildSpecialVariables() {
    final List<String> vars = new ArrayList<>();

    for (KomaVar komaVar : komavars) {
      vars.add(komaVar.toString());
    }

    final String result = String.join("\n", vars);
    return result;
  }
}
