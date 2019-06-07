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

  /**
   * @param message the contract to be fulfilled
   * @param cause   the cause for this violation
   */
  public ContractBrokenException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause the cause for this violation
   */
  public ContractBrokenException(Throwable cause) {
    super(cause);
  }
}
