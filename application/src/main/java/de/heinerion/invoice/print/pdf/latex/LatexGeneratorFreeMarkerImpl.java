package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.models.*;
import freemarker.template.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service("FreemarkerImpl")
class LatexGeneratorFreeMarkerImpl implements LatexGenerator {
  private static Configuration cfg = null;

  static {
    initConfig();
  }

  private static void initConfig() {
    if (cfg == null) {
      cfg = new Configuration(Configuration.VERSION_2_3_28);
      cfg.setClassForTemplateLoading(LatexGeneratorFreeMarkerImpl.class, "templates");
      cfg.setDefaultEncoding("UTF-8");
      cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
      cfg.setLogTemplateExceptions(false);
      cfg.setWrapUncheckedExceptions(true);
      cfg.setDateFormat("dd.MM.yyyy");
      cfg.setNumberFormat("0.00");
    }
  }

  @Override
  public String generateSourceContent(Conveyable conveyable) {
    Variant variant = conveyable instanceof Invoice ? Variant.INVOICE : Variant.LETTER;

    try {
      Map<String, Object> root = new HashMap<>();
      root.put(variant.rootElement, conveyable);
      root.put("fromaddress", createFromAddress(conveyable.getCompany()));
      root.put("labels", createLabels(variant));
      root.put("address", conveyable.getReceiver().getLinesNonEmpty());

      Template temp = cfg.getTemplate(variant.template);
      StringWriter writer = new StringWriter();
      temp.process(root, writer);
      return writer.toString();
    } catch (IOException | TemplateException e) {
      throw new LatexGeneratorException(e);
    }
  }

  private String createFromAddress(Company company) {
    return String.join(", ", company.getAddress().getLinesNoNameNonEmpty());
  }

  private Map<String, String> createLabels(Variant variant) {
    Map<String, String> result = new HashMap<>();

    result.put("description", Translator.translate("invoice.description"));
    result.put("pricePerUnit", Translator.translate("invoice.pricePerUnit"));
    result.put("sum", Translator.translate("invoice.sum"));

    result.put("net", Translator.translate("invoice.net"));
    result.put("vat", Translator.translate("invoice.vat"));
    result.put("signature", Translator.translate("invoice.signature"));

    result.put("title", Translator.translate(variant.title));
    result.put("date", Translator.translate("invoice.date"));

    result.put("number", Translator.translate("invoice.number.short"));
    result.put("taxNumber", Translator.translate("invoice.taxNumber"));

    return result;
  }

  private enum Variant {
    LETTER, INVOICE;

    final String rootElement;
    final String template;
    final String title;

    Variant() {
      String key = this.name().toLowerCase();
      this.rootElement = key;
      this.template = key + ".tex";
      this.title = key + ".title";
    }
  }

  private static class LatexGeneratorException extends RuntimeException {
    LatexGeneratorException(Throwable e) {
      super("The template could not be processed", e);
    }
  }
}
