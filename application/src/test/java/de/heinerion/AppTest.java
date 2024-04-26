package de.heinerion;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppTest {
  @BeforeAll
  public static void setupHeadlessMode() {
    System.setProperty("java.awt.headless", "false");
  }

  @Test
  @DisplayName("Should load the complete context without errors")
  public void shouldLoadTheCompleteContextWithoutErrors() {
    // no assertion
    // any problems in spring's service loading will let this test fail
  }
}
