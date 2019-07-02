package de.heinerion.invoice.tool.business;

import de.heinerion.invoice.tool.boundary.FileService;
import de.heinerion.invoice.tool.boundary.templating.FreeMarkerLatexGenerator;
import de.heinerion.invoice.tool.domain.Document;
import de.heinerion.invoice.tool.domain.Invoice;
import de.heinerion.invoice.tool.domain.Letter;

public class PrintService {
  private FileService fileService;

  public void setFileService(FileService fileService) {
    this.fileService = fileService;
  }

  public void print(String path, String baseName, Document document) {
    if (document instanceof Invoice) {
      print(path, baseName, (Invoice) document);
    } else {
      print(path, baseName, (Letter) document);
    }
  }

  public void print(String path, String baseName, Invoice invoice) {
    if (fileService.writeTex(path, baseName, generateTex(invoice))) {
      fileService.texToPdf(path, baseName);
    }
  }

  private String generateTex(Invoice invoice) {
    FreeMarkerLatexGenerator generator = new FreeMarkerLatexGenerator();
    return generator.generateSourceContent(invoice);
  }

  public void print(String path, String baseName, Letter letter) {
    if (fileService.writeTex(path, baseName, generateTex(letter))) {
      fileService.texToPdf(path, baseName);
    }
  }

  private String generateTex(Letter letter) {
    FreeMarkerLatexGenerator generator = new FreeMarkerLatexGenerator();
    return generator.generateSourceContent(letter);
  }
}
