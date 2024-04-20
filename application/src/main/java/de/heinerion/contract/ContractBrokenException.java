package de.heinerion.contract;

/**
 * Exception to express that a vital contract has been violated
 */
public class ContractBrokenException extends RuntimeException {
  /**
   * @param message the contract to be fulfilled
   */
  public ContractBrokenException(String message) {
    super(message);
  }
}
