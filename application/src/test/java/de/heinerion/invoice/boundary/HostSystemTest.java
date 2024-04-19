package de.heinerion.invoice.boundary;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HostSystemTest {

  @Test
  void createFile(@TempDir Path dir) {
    HostSystem host = new HostSystem();
    Path path = dir.resolve("anything");
    assertTrue(host.createFile(path), "File created");
    assertFalse(host.createFile(path), "The file was already created");
  }
}