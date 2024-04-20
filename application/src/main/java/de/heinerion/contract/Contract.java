package de.heinerion.contract;

/**
 * Offers static Methods to formulate contracts in code.
 * <p>
 * The Messages passed to the contracts are meant to express the contract, not the failure.
 * </p>
 * <p>
 * <b>Example</b><br>
 * In this example two contracts are to be fulfilled
 * <ol>
 * <li>bar is required not to be null</li>
 * <li>the method ensures, that result is never empty</li>
 * </ol>
 * </p>
 * <pre><code>
 * private String foo(Object bar) {
 *    Contract.requireNotNull(bar, "bar");
 *    String result = bar.toString();
 *    Contract.ensure(!result.isEmpty(), "result is not empty");
 *    return result;
 * }
 * </code></pre>
 */
public class Contract {
  /**
   * This class should not be instantiated, so we avoid it
   */
  private Contract() {
    // hidden
  }

  /**
   * Requires the given {@link Object} not to be {@code null}
   * <p>
   * Shall be used at the beginning of methods, meant as a guard of pre-conditions
   * </p>
   *
   * @param variable     shall not be {@code null}
   * @param variableName name of the given variable, to be used in the exception message if violated
   * @throws ContractBrokenException if the contract is violated
   */
  public static void requireNotNull(Object variable, String variableName) {
    if (variable == null)
      throw new ContractBrokenException("%s is not null".formatted(variableName));
  }

  /**
   * Requires the given {@link Object} not to be {@code null}
   * <p>
   * Shall be used at the beginning of methods, meant as a guard of pre-conditions
   * </p>
   *
   * @param variable     shall not be {@code null}
   * @param variableName name of the given variable, to be used in the exception message by violation
   * @param context      context of this contract, which will be appended to the exception, if violated
   * @throws ContractBrokenException if the contract is violated
   */
  public static void requireNotNull(Object variable, String variableName, String context) {
    if (variable == null)
      throw new ContractBrokenException("%s is not null. (%s)".formatted(variableName, context));
  }

  /**
   * Ensures the given {@link Object} is not null after processing
   * <p>
   * Shall be used at the end or well inside of methods, meant as a guard of post-conditions
   * </p>
   *
   * @param variable     shall not be {@code null}
   * @param variableName name of the given variable, to be used in the exception message by violation
   * @throws ContractBrokenException if the contract is violated
   */
  public static void ensureNotNull(Object variable, String variableName) {
    requireNotNull(variable, variableName);
  }

  /**
   * Requires the given condition to be met.
   * <p>
   * Shall be used at the beginning of methods, meant as a guard of pre-conditions
   * </p>
   *
   * @param condition    shall not result in {@code false}
   * @param errorMessage message to express the contract
   * @throws ContractBrokenException if the contract is violated
   */
  public static void require(boolean condition, String errorMessage) {
    if (!condition)
      throw new ContractBrokenException(errorMessage);
  }
}
