package de.heinerion.invoice.tool.business;

import de.heinerion.invoice.tool.boundary.FileService;
import de.heinerion.invoice.tool.domain.*;

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
            "%s %s - %s %s - %S" +
            "\\end{document}",
        company.getName(), String.join(", ", company.getAddress()),
        customer.getName(), String.join(", ", customer.getAddress()),
        invoice.getItems().stream().map(InvoiceItem::toString).collect(Collectors.joining(",")));
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
            "%s %s - %s %s - %s" +
            "\\end{document}",
        company.getName(), String.join(", ", company.getAddress()),
        customer.getName(), String.join(", ", customer.getAddress()),
        letter.getText());
  }
}
