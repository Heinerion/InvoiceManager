package de.heinerion.invoice.tool.boundary.templating;

import de.heinerion.invoice.tool.boundary.Translator;
import de.heinerion.invoice.tool.domain.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class FreeMarkerLatexGenerator {
  private static final Configuration CONFIGURATION;

  static {
    CONFIGURATION = new Configuration(Configuration.VERSION_2_3_28);
    CONFIGURATION.setClassForTemplateLoading(FreeMarkerLatexGenerator.class, "/templates");
    CONFIGURATION.setDefaultEncoding("UTF-8");
    CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    CONFIGURATION.setLogTemplateExceptions(false);
    CONFIGURATION.setWrapUncheckedExceptions(true);
  }

  public String generateSourceContent(Document document) {
    try {
      Map<String, Object> root = new HashMap<>();

      root.put("address", document.getCustomer().getAddress());
      root.put("date", document.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
      root.put("subject", document.getSubject());

      root.put("company", createCompanyInfo(document.getCompany()));
      root.put("labels", createLabels());
      root.put("pdf", createPdfInfo(document));

      if (document instanceof Letter) {
        root.put("textLines", Arrays.asList(((Letter) document).getText().split("\n")));
      } else {
        root.put("invoice", createInvoiceInfo((Invoice) document));
      }
      root.put("pageFooter", String.join(", ", document.getCompany().getName(), document.getSubject(), document.getDate().toString()));

      Template temp = CONFIGURATION.getTemplate(document instanceof Letter ? "letter.tex" : "invoice.tex");
      StringWriter writer = new StringWriter();
      temp.process(root, writer);
      return writer.toString();
    } catch (IOException | TemplateException e) {
      throw new RuntimeException("The template could not be processed", e);
    }
  }

  private Map<String, Object> createInvoiceInfo(Invoice invoice) {
    Map<String, Object> result = new HashMap<>();

    result.put("number", invoice.getNumber());
    result.put("items", createItemList(invoice));
    result.put("net", escape(invoice.getNetSum()));
    result.put("taxes", createTaxInfos(invoice));
    result.put("gross", escape(invoice.getGrossSum()));

    return result;
  }

  private Collection<Map<String, Object>> createTaxInfos(Invoice invoice) {
    Collection<Map<String, Object>> result = new LinkedList<>();

    Map<Percent, Euro> taxes = new TreeMap<>(invoice.getTaxes());
    for (Map.Entry<Percent, Euro> percentEuroEntry : taxes.entrySet()) {
      Map<String, Object> line = new HashMap<>();
      line.put("percentage", escape(percentEuroEntry.getKey()));
      line.put("value", escape(percentEuroEntry.getValue()));
      result.add(line);
    }

    return result;
  }

  private Collection<Map<String, Object>> createItemList(Invoice invoice) {
    Collection<Map<String, Object>> result = new LinkedList<>();

    Collection<InvoiceItem> items = invoice.getItems();
    for (InvoiceItem item : items) {
      Map<String, Object> properties = new HashMap<>();
      Product product = item.getProduct();
      properties.put("name", product.getName());
      properties.put("unit", product.getUnit());
      properties.put("pricePerUnit", escape(product.getPricePerUnit()));
      properties.put("quantity", item.getCount());
      properties.put("total", escape(item.getNetPrice()));

      result.add(properties);
    }

    return result;
  }

  private Map<String, Object> createCompanyInfo(Company company) {
    Map<String, Object> result = new HashMap<>();

    result.put("name", company.getName());
    result.put("phone", company.getPhone());
    result.put("address", company.getAddress());
    result.put("signature", company.getCorrespondent().orElse(company.getName()));

    result.put("bankName", company.getBankName());
    result.put("iban", company.getIban());
    result.put("bic", company.getBic());
    result.put("taxNumber", company.getTaxNumber());

    return result;
  }

  private Map<String, String> createPdfInfo(Document document) {
    Map<String, String> result = new HashMap<>();

    result.put("title", document.getSubject());
    result.put("author", document.getCompany().getName());
    result.put("subject", document.getSubject());
    result.put("keywords", String.join(", ", document.getKeywords()));

    return result;
  }

  private Map<String, String> createLabels() {
    Map<String, String> result = new HashMap<>();

    List<String> keys = Arrays.asList(
        "date",
        "description",
        "net",
        "number",
        "of",
        "page",
        "pricePerUnit",
        "sum",
        "taxNumber",
        "vat"
    );

    for (String key : keys) {
      result.put(key, Translator.translate(key));
    }

    return result;
  }

  private String escape(Euro euro) {
    return String.format("\\EUR{%d,%02d}", euro.getEuros(), euro.getCents());
  }

  private String escape(Percent percent) {
    return String.format("%s \\%%", percent.getPercentage());
  }
}
