package broccoli.common;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

/**
 * The {@link TransactionHelper} class.
 */
@Singleton
public class TransactionHelper {

  /**
   * Execute within transaction.
   *
   * @param action the action
   */
  @Transactional
  public void executeWithinTransaction(Runnable action) {
    // Business logic here
    action.run();

    // Transaction will be committed after this method
    // or rolled back if there is an exception
  }
}
