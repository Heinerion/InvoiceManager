package de.heinerion;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppTest {
  @BeforeAll
  public static void setupHeadlessMode() {
    System.setProperty("java.awt.headless", "false");
  }

  @Test
  void contextLoads() {
  }
}
