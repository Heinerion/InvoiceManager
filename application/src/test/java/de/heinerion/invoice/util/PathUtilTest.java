package de.heinerion.invoice.util;

import de.heinerion.invoice.models.Invoice;
import de.heinerion.invoice.models.Letter;
import de.heinerion.invoice.services.ConfigurationService;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static de.heinerion.invoice.services.ConfigurationService.PropertyKey.*;
import static org.junit.Assert.assertEquals;

public class PathUtilTest {
  private static String baseDir;

  private static String combine(String... token) {
    return String.join(File.separator, token);
  }

  private static String property(ConfigurationService.PropertyKey key) {
    return ConfigurationService.get(key);
  }

  private static PathUtilNG pathUtil;

  @Before
  public void setUp() {
    baseDir = combine(java.lang.System.getProperty("user.home"), property(FOLDER_DATA));

    pathUtil = new PathUtilBuilder().build();
  }

  @Test
  public void getSystemPath() {
    String expected = combine(baseDir, property(FOLDER_SYSTEM));
    assertEquals(expected, pathUtil.getSystemPath());
  }

  @Test
  public void determineLetterPath() {
    String expected = combine(baseDir, property(FOLDER_LETTERS));
    assertEquals(expected, pathUtil.determinePath(Letter.class));
  }

  @Test
  public void determineInvoicePath() {
    String expected = combine(baseDir, property(FOLDER_INVOICES));
    assertEquals(expected, pathUtil.determinePath(Invoice.class));
  }

  @Test
  public void getBaseDir() {
    assertEquals(baseDir, pathUtil.getBaseDir());
  }

  @Test
  public void getTemplateFileName() {
    String descriptiveName = "descriptiveName";
    String expected = combine(baseDir, property(FOLDER_SYSTEM), property(FOLDER_TEMPLATES), descriptiveName + ".sav");
    assertEquals(expected, pathUtil.getTemplateFileName(descriptiveName));
  }
}