package de.heinerion.invoice.util;

/**
 * Builder for {@link PathUtilNG} for use in classes without {@link org.springframework.beans.factory.annotation.Autowire} capabilities
 */
public class PathUtilBuilder {
  public PathUtilNG build() {
    return new PathUtilNG();
  }
}
