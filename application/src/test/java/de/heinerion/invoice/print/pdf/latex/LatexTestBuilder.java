package de.heinerion.invoice.print.pdf.latex;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LatexTestBuilder {

  private static final Month MONTH = Month.JUNE;
  private static final int YEAR = 2010;
  private static final int DAY = 25;
  private static final String TWEAKS = "\\usepackage{etoolbox}% http://ctan.org/pkg/etoolbox\n"
      + "\\makeatletter\n"
      + "\n"
      + "% allows for more horizontal space in the location\n"
      + "\\@setplength{lochpos}{\\oddsidemargin}\n"
      + "\\@addtoplength{lochpos}{6cm}\n"
      + "\n"
      + "% pull signature closer to the table\n"
      + "\\patchcmd{\\closing}% <cmd>\n"
      + "  {\\parbox}% <search>\n"
      + "  {\\parbox{\\linewidth}{\\raggedsignature\\strut\\ignorespaces\\let\\\\\\relax%\n"
      + "      #1 \\usekomavar{signature}}%\n"
      + "   \\@gobbletwo}%< <replace>\n"
      + "  {}{}% <success><failure>\n"
      + "\\makeatother"
      + "\n"
      + "\n";

  static Builder builder() {
    return new Builder();
  }

  static class Builder {
    private String articles;
    private String tax;
    private String net;
    private String gross;
    private String sum;
    private final List<String> lines = new ArrayList<>();

    public Builder withArticles(String articles) {
      this.articles = articles;
      return this;
    }

    public Builder withTax(String tax) {
      this.tax = tax;
      return this;
    }

    public Builder withNet(String net) {
      this.net = net;
      return this;
    }

    public Builder withGross(String gross) {
      this.gross = gross;
      return this;
    }

    /**
     * same value as gross, but decimal delimiter is '.'
     */
    public Builder withSum(String sum) {
      this.sum = sum;
      return this;
    }

    public Builder withLine(String line) {
      lines.add(line);
      return this;
    }

    public String build() {
      Objects.requireNonNull(articles, "articles");
      Objects.requireNonNull(tax, "tax");
      Objects.requireNonNull(net, "net");
      Objects.requireNonNull(gross, "gross");
      Objects.requireNonNull(sum, "sum");

      String preface = "\\documentclass"
          + "[fontsize=12pt, fromalign=center, fromphone=true, paper=a4]"
          + "{scrlttr2}\n"
          + "\\usepackage[utf8]{inputenc}\n"
          + "\\usepackage[ngermanb]{babel}\n"
          + "\\usepackage[right]{eurosym}\n"
          + "\\usepackage{longtable}\n"
          + "\\usepackage{lastpage}             % determine last page\n"
          + "\\usepackage{scrlayer-scrpage}     % scr-Styling\n"
          + "\\usepackage[hidelinks]{hyperref}  % use hyperref without link highlighting\n"
          + "\\hypersetup{\n  pdftitle={Rechnung},\n  pdfauthor={officialName},\n  "
          + "pdfsubject={"
          + articles
          + "},\n  pdfkeywords={"
          + sum
          + "}\n}\n"
          + "\\renewcommand{\\raggedsignature}"
          + "{\\raggedright}\n"
          + "\\setkomavar{signature}"
          + "{\\underline{Unterschrift:\\hspace{10cm}}}\n"
          + "\\setkomavar{subject}{Rechnung}\n"
          + "\\setkomavar{fromaddress}"
          + "{street number, postalCode location}\n"
          + "\\setkomavar{fromphone}{123-456}\n"
          + "\\setkomavar{fromname}{{officialName}\\tiny}\n"
          + "\\date{}\n"
          + "\\setkomavar{location}{\n"
          + "  \\vfill\n"
          + "  \\raggedright\n"
          + "  \\footnotesize\n"
          + "  \\begin{tabular}{ll}\n"
          + "  \\textsc Datum & : "
          + String.join(".", DAY + "", (MONTH.getValue() < 10 ? "0" : "")
          + MONTH.getValue(), YEAR + "")
          + "\\\\\n"
          + "  \\textsc Rechnungs-Nr. & : 0\\\\\n"
          + "  \\textsc Steuernummer & : taxNumber\\\\\n"
          + "  \\textsc BIC & : bic\\\\\n"
          + "  \\textsc IBAN & : iban\\\\\n"
          + "  \\multicolumn{2}{l}{institute}\n"
          + "  \\end{tabular}\n" + "}\n"
          + "\n"
          + "\\setkomavar{firstfoot}{\n"
          + "  \\usekomafont{pageheadfoot}\n"
          + "  \\parbox{\\useplength{firstfootwidth}}{\n"
          + "    \\rule{\\linewidth}{.4pt}\\\\\n"
          + "    \\parbox[t]{0.8\\linewidth}{\n"
          + "      officialName, Rechnungs-Nr.: 0, 25.06.2010\n"
          + "    }\n"
          + "    \\hfill\n"
          + "    \\parbox[t]{0.1\\linewidth}{\n"
          + "      Seite \\thepage \\hspace{1pt} von \\pageref{LastPage}\n"
          + "    }\n"
          + "  }\n"
          + "}\n"
          + "\\setkomafont{pageheadfoot}{\\scriptsize}\n"
          + "\n"
          + "% Use the footer-style of the first page in following pages\n"
          + "\\DeclareNewLayer[\n"
          + "  foreground,\n"
          + "  textarea,\n"
          + "  voffset=\\useplength{firstfootvpos},\n"
          + "  hoffset=\\dimexpr.5\\paperwidth-.5\\useplength{firstfootwidth}\\relax,\n"
          + "  width=\\useplength{firstfootwidth},\n"
          + "  mode=picture,\n"
          + "  contents=\\putUL{\\raisebox{\\dimexpr-\\height}{\\usekomavar{firstfoot}}}\n"
          + "]{likefirstpage.foot}\n"
          + "\n"
          + "\\AddLayersToPageStyle{scrheadings}{likefirstpage.foot}\n"
          + "\\clearpairofpagestyles\n"
          + "\n"
          + TWEAKS
          + "\\begin{document}\n"
          + "\\begin{letter}{formatted\\\\\n"
          + "address}\n\n"
          + "\\opening{}\\vspace{-25pt}\n"
          + "\\begin{longtable}{p{5cm}p{1cm}p{3cm}|r|r|}\n"
          + "\\hline"
          + "\n"
          + "\\multicolumn{3}{|c|}{\\textsc Bezeichnung}&"
          + "\\multicolumn{1}{c|}{\\textsc Einzelpreis}&"
          + "\\multicolumn{1}{c|}{\\textsc Gesamt}\\\\\n"
          + "\\hline"
          + "\n"
          + "\\hline"
          + "\n";



      String articles = lines.stream().map(s -> s + "\\hline" + "\n").collect(Collectors.joining());
      for (int i = lines.size(); i < 5; i++) {
        articles += "\\multicolumn{1}{|l}{$\\phantom{sth}$}&&&&\\\\\n"
            + "\\hline"
            + "\n";
      }

      return preface
          + articles
          + "\\multicolumn{1}{|l}{$\\phantom{sth}$}&"
          + "\\multicolumn{1}{r}{\\phantom{(\\hfill000,00}}&&&\\\\\n"
          + "\\hline"
          + "\n"
          + "\\multicolumn{1}{|l}{Netto}&&&&\\EUR{"
          + net
          + "}\\\\\n"
          + "\\hline"
          + "\n"
          + "&&&\\multicolumn{1}{|c|}{10,00\\% MwSt}&\\EUR{"
          + tax
          + "}\\\\\\cline{4-5}\n"
          + "&&&\\multicolumn{1}{|l|}{\\textsc Gesamt}&\\EUR{"
          + gross
          + "}\\\\\\cline{4-5}\n"
          + "\\end{longtable}\n"
          + "\\vfill\n"
          + "\\closing{}\n"
          + "\n"
          + "\\end{letter}\n"
          + "\\end{document}";
    }
  }
}
