package de.heinerion;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("Test")
public class TestConfiguration {
  // Needed to tell Spring Boot to set the following Property.
  // The properties itself prevents swing from failing
  static {
    System.setProperty("java.awt.headless", "false");
  }
}
