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

  @Before
  public void setUp() throws Exception {
    baseDir = combine(java.lang.System.getProperty("user.home"), property("folder.data"));
  }

  @Test
  public void getSystemPath() throws Exception {
    String expected = combine(baseDir, property("folder.system"));
    assertEquals(expected, PathUtil.getSystemPath());
  }

  @Test
  public void getTemplatePath() throws Exception {
    String expected = combine(baseDir, property("folder.system"), property("folder.templates"));
    assertEquals(expected, PathUtil.getTemplatePath());
  }

  @Test
  public void getTemplateFolderName() throws Exception {
    assertEquals(property("folder.templates"), PathUtil.getTemplateFolderName());
  }

  @Test
  public void getTexTemplatePath() throws Exception {
    String expected = combine(baseDir, property("folder.texTemplates"));
    assertEquals(expected, PathUtil.getTexTemplatePath());
  }

  @Test
  public void determineLetterPath() throws Exception {
    String expected = combine(baseDir, property("folder.letters"));
    assertEquals(expected, PathUtil.determinePath(Letter.class));
  }

  @Test
  public void determineInvoicePath() throws Exception {
    String expected = combine(baseDir, property("folder.invoices"));
    assertEquals(expected, PathUtil.determinePath(Invoice.class));
  }

  @Test
  public void getBaseDir() throws Exception {
    assertEquals(baseDir, PathUtil.getBaseDir());
  }

  @Test
  public void getTemplateFileName() throws Exception {
    String descriptiveName = "descriptiveName";
    String expected = combine(baseDir, property("folder.system"), property("folder.templates"), descriptiveName + ".sav");
    assertEquals(expected, PathUtil.getTemplateFileName(descriptiveName));
  }
}