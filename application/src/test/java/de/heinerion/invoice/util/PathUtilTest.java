package de.heinerion.invoice.util;

import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.services.ConfigurationService;
import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static de.heinerion.invoice.services.ConfigurationService.PropertyKey.*;
import static org.junit.Assert.assertEquals;

public class PathUtilTest {
  private static String baseDir;

  private static Company company;

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
    company = new CompanyBuilder().withDescriptiveName("descriptive").build();
    pathUtil = new PathUtilNG();
  }

  @Test
  public void getSystemPath() {
    String expected = combine(baseDir, property(FOLDER_SYSTEM));
    assertEquals(expected, pathUtil.getSystemPath().toString());
  }

  @Test
  public void determineLetterPath() {
    String expected = combine(baseDir, property(FOLDER_LETTERS));
    assertEquals(expected, pathUtil.determineLetterPath().toString());
  }

  @Test
  public void determineInvoicePath() {
    String expected = combine(baseDir, property(FOLDER_INVOICES), company.getDescriptiveName());
    assertEquals(expected, pathUtil.determineInvoicePath(company).toString());
  }

  @Test
  public void getBaseDir() {
    assertEquals(baseDir, pathUtil.getBasePath().toString());
  }

  @Test
  public void getTemplateFileName() {
    String descriptiveName = "descriptiveName";
    String expected = combine(baseDir, property(FOLDER_SYSTEM), property(FOLDER_TEMPLATES), descriptiveName + ".sav");
    assertEquals(expected, pathUtil.getTemplateFilePath(descriptiveName).toString());
  }
}