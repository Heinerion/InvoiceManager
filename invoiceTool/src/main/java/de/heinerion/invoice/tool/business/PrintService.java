package de.heinerion.invoice.tool.business;

import de.heinerion.invoice.tool.boundary.FileService;
import de.heinerion.invoice.tool.domain.Customer;
import de.heinerion.invoice.tool.domain.Invoice;
import de.heinerion.invoice.tool.domain.InvoiceItem;
import de.heinerion.invoice.tool.domain.Letter;

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
    Customer customer = invoice.getCustomer();
    return String.format("" +
            "\\documentclass[fontsize=12pt, fromalign=center, fromphone=true, paper=a4]{scrlttr2}" +
            "\\begin{document}" +
            "-- tex placeholder -- %s %s %s" +
            "\\end{document}",
        String.join(", ", customer.getAddress()),
        customer.getName(),
        invoice.getItems().stream().map(InvoiceItem::toString).collect(Collectors.joining(",")));
  }

  public void print(String path, String baseName, Letter letter) {
    if (fileService.writeTex(path, baseName, generateTex(letter))) {
      fileService.texToPdf(path, baseName);
    }
  }

  private String generateTex(Letter letter) {
    Customer customer = letter.getCustomer();
    return String.format("" +
            "\\documentclass[fontsize=12pt, fromalign=center, fromphone=true, paper=a4]{scrlttr2}" +
            "\\begin{document}" +
            "-- tex placeholder -- %s %s %s" +
            "\\end{document}",
        String.join(", ", customer.getAddress()), customer.getName(), letter.getText());
  }
}
