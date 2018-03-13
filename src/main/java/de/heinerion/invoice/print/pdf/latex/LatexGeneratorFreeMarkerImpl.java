package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.invoice.view.formatter.Formatter;
import de.heinerion.invoice.Translator;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

class LatexGeneratorFreeMarkerImpl implements LatexGenerator {
  private static Configuration cfg = null;

  static {
    initConfig();
  }

  private final Formatter formatter;

  @Autowired
  LatexGeneratorFreeMarkerImpl(Formatter formatter) {
    this.formatter = formatter;
  }

  private static void initConfig() {
    if (cfg == null) {
      cfg = new Configuration(Configuration.VERSION_2_3_27);
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
  public String generateSourceContent(Letter letter) {
    Variant variant = letter instanceof Invoice ? Variant.INVOICE : Variant.LETTER;

    try {
      Map<String, Object> root = new HashMap<>();
      root.put(variant.rootElement, letter);
      root.put("labels", createLabels(variant));
      root.put("address", formatter.formatAddress(letter.getReceiver()));

      Template temp = cfg.getTemplate(variant.template);
      StringWriter writer = new StringWriter();
      temp.process(root, writer);
      return writer.toString();
    } catch (IOException | TemplateException e) {
      throw new LatexGeneratorException(e);
    }
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

    String rootElement;
    String template;
    String title;

    Variant() {
      String key = this.name().toLowerCase();
      this.rootElement = key;
      this.template = key + ".tex";
      this.title = key + ".title";
    }
  }

  private class LatexGeneratorException extends RuntimeException {
    LatexGeneratorException(Throwable e) {
      super ("The template could not be processed", e);
    }
  }
}
