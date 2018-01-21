package de.heinerion.betriebe.util;

import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.services.ConfigurationService;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class PathUtilTest {
  private static String baseDir;

  private static String combine(String... token) {
    return String.join(File.separator, token);
  }

  private static String property(String key) {
    return ConfigurationService.get(key);
  }

  private static PathUtilNG pathUtil;

  @Before
  public void setUp() {
    baseDir = combine(java.lang.System.getProperty("user.home"), property("folder.data"));

    pathUtil = new PathUtilBuilder().build();
  }

  @Test
  public void getSystemPath() {
    String expected = combine(baseDir, property("folder.system"));
    assertEquals(expected, pathUtil.getSystemPath());
  }

  @Test
  public void getTemplatePath() {
    String expected = combine(baseDir, property("folder.system"), property("folder.templates"));
    assertEquals(expected, pathUtil.getTemplatePath());
  }

  @Test
  public void getTemplateFolderName() {
    assertEquals(property("folder.templates"), pathUtil.getTemplateFolderName());
  }

  @Test
  public void getTexTemplatePath() {
    String expected = combine(baseDir, property("folder.texTemplates"));
    assertEquals(expected, pathUtil.getTexTemplatePath());
  }

  @Test
  public void determineLetterPath() {
    String expected = combine(baseDir, property("folder.letters"));
    assertEquals(expected, pathUtil.determinePath(Letter.class));
  }

  @Test
  public void determineInvoicePath() {
    String expected = combine(baseDir, property("folder.invoices"));
    assertEquals(expected, pathUtil.determinePath(Invoice.class));
  }

  @Test
  public void getBaseDir() {
    assertEquals(baseDir, pathUtil.getBaseDir());
  }

  @Test
  public void getTemplateFileName() {
    String descriptiveName = "descriptiveName";
    String expected = combine(baseDir, property("folder.system"), property("folder.templates"), descriptiveName + ".sav");
    assertEquals(expected, pathUtil.getTemplateFileName(descriptiveName));
  }
}