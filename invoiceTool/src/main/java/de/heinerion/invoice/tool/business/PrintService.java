package de.heinerion.invoice.tool.business;

import de.heinerion.invoice.tool.boundary.FileService;
import de.heinerion.invoice.tool.domain.*;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Collectors;

public class PrintService {
  private FileService fileService;

  public void setFileService(FileService fileService) {
    this.fileService = fileService;
  }

  public void print(String path, String baseName, Invoice invoice) {
    if (fileService.writeTex(path, baseName, generateTex(invoice))) {
      fileService.texToPdf(path, baseName);
    }
  }

  private String generateTex(Invoice invoice) {
    Company company = invoice.getCompany();
    Customer customer = invoice.getCustomer();
    return String.format("" +
            "\\documentclass[fontsize=12pt, fromalign=center, fromphone=true, paper=a4]{scrlttr2}" +
            "\\begin{document}" +
            "-- tex placeholder --" +
            "[%s] %s %s - %s %s - %S @%s" +
            "(%s)" +
            "\\end{document}",
        invoice.getSubject(),
        company.getName(), String.join(", ", company.getAddress()),
        customer.getName(), String.join(", ", customer.getAddress()),
        invoice.getItems().stream().map(InvoiceItem::toString).collect(Collectors.joining(",")),
        invoice.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
        String.join(", ", invoice.getKeywords())
    );
  }

  public void print(String path, String baseName, Letter letter) {
    if (fileService.writeTex(path, baseName, generateTex(letter))) {
      fileService.texToPdf(path, baseName);
    }
  }

  private String generateTex(Letter letter) {
    Company company = letter.getCompany();
    Customer customer = letter.getCustomer();

    return String.format("" +
            "\\documentclass[fontsize=12pt, fromalign=center, fromphone=true, paper=a4]{scrlttr2}" +
            "\\begin{document}" +
            "-- tex placeholder --" +
            "[%s] %s %s - %s %s - %s @%s" +
            "(%s)" +
            "\\end{document}",
        letter.getSubject(),
        company.getName(), String.join(", ", company.getAddress()),
        customer.getName(), String.join(", ", customer.getAddress()),
        letter.getText(), letter.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
        String.join(", ", letter.getKeywords())
    );
  }
}
