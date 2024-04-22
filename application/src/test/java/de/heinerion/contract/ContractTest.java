package de.heinerion.contract;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ContractTest {

  @Test
  void requireNotNull() {
    Contract.requireNotNull("", "name");
    ContractBrokenException cbe = assertThrows(ContractBrokenException.class,
        () -> Contract.requireNotNull(null, "name"));
    assertEquals("name is not null", cbe.getMessage());
  }

  @Test
  void ensureNotNull() {
    Contract.ensureNotNull("", "name");
    ContractBrokenException cbe = assertThrows(ContractBrokenException.class,
        () -> Contract.ensureNotNull(null, "name"));
    assertEquals("name is not null", cbe.getMessage());
  }

  @Test
  void require() {
    Contract.require(true, "true is true");
    ContractBrokenException cbe = assertThrows(ContractBrokenException.class,
        () -> Contract.require(false, "false is false"));
    assertEquals("false is false", cbe.getMessage());
  }
}