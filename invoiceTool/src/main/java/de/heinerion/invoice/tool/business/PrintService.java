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

  public void print(String path, Invoice invoice) {
    if (fileService.writeTex(path, generateTex(invoice))) {
      fileService.texToPdf(path);
    }
  }

  private String generateTex(Invoice invoice) {
    Customer customer = invoice.getCustomer();
    return String.format("-- tex placeholder -- %s %s %s",
        String.join(", ", customer.getAddress()),
        customer.getName(),
        invoice.getItems().stream().map(InvoiceItem::toString).collect(Collectors.joining(",")));
  }

  public void print(String path, Letter letter) {
    if (fileService.writeTex(path, generateTex(letter))) {
      fileService.texToPdf(path);
    }
  }

  private String generateTex(Letter letter) {
    Customer customer = letter.getCustomer();
    return String.format("-- tex placeholder -- %s %s %s", String.join(", ", customer.getAddress()), customer.getName(), letter.getText());
  }
}
