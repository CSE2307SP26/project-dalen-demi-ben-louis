package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.BankAccount;

//All tests follow the same structure:
//1. Create necessary object(s)
//2. Call the method(s) being tested
//3. Use assertion(s) to validate the result(s)

public class BankAccountTest {

	private BankAccount testAccount;

	@BeforeEach
	public void setUp() {
		testAccount = new BankAccount();
	}

	// 1. A deposit of a valid amount should increase the balance
	@Test
	void testDeposit() {
		double expectedBalance = 50;
		testAccount.deposit(50);
		double actualBalance = testAccount.getBalance();
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 2. A deposit of a negative amount should throw an exception
	@Test
	void testInvalidDeposit() {
		assertThrows(IllegalArgumentException.class, () -> {
			testAccount.deposit(-50);
		});
	}

	// 3. A withdrawal of a valid amount should decrease the balance
	@Test
	void testWithdrawValidAmount() {
		double expectedBalance = 50;
		testAccount.deposit(100);
		testAccount.withdraw(50);
		double actualBalance = testAccount.getBalance();
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 4. Withdrawing the entire balance should bring the balance to zero
	@Test
	void testWithdrawEntireBalance() {
		double expectedBalance = 0;
		testAccount.deposit(100);
		testAccount.withdraw(100);
		double actualBalance = testAccount.getBalance();
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 5. Withdrawing more than the balance should throw an exception
	@Test
	void testWithdrawInsufficientFunds() {
		testAccount.deposit(50);
		assertThrows(IllegalArgumentException.class, () -> {
			testAccount.withdraw(100);
		});
	}

	// 6. Withdrawing a negative amount should throw an exception
	@Test
	void testWithdrawNegativeAmount() {
		testAccount.deposit(100);
		assertThrows(IllegalArgumentException.class, () -> {
			testAccount.withdraw(-50);
		});
	}

	// 7. Withdrawing zero should throw an exception
	@Test
	void testWithdrawZeroAmount() {
		testAccount.deposit(100);
		assertThrows(IllegalArgumentException.class, () -> {
			testAccount.withdraw(0);
		});
	}

	// 8. Withdrawing from an account with zero balance should throw an exception
	@Test
	void testWithdrawFromEmptyAccount() {
		assertThrows(IllegalArgumentException.class, () -> {
			testAccount.withdraw(10);
		});
	}

	// 9. Multiple withdrawals should correctly decrease the balance each time
	@Test
	void testMultipleWithdrawals() {
		double expectedBalance = 40;
		testAccount.deposit(100);
		testAccount.withdraw(30);
		testAccount.withdraw(30);
		double actualBalance = testAccount.getBalance();
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 10. The balance should not change when a withdrawal is rejected due to insufficient funds
	@Test
	void testBalanceUnchangedAfterFailedWithdraw() {
		double expectedBalance = 50;
		testAccount.deposit(50);
		try {
			testAccount.withdraw(100);
		} catch (IllegalArgumentException e) {
			// expected
		}
		double actualBalance = testAccount.getBalance();
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 11. A new account should have a balance of zero
	@Test
	void testNewAccountBalanceIsZero() {
		double expectedBalance = 0;
		double actualBalance = testAccount.getBalance();
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 12. Balance should accurately reflect a single deposit
	@Test
	void testBalanceAfterSingleDeposit() {
		double expectedBalance = 200;
		testAccount.deposit(200);
		double actualBalance = testAccount.getBalance();
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 13. Balance should accurately reflect multiple deposits
	@Test
	void testBalanceAfterMultipleDeposits() {
		double expectedBalance = 150;
		testAccount.deposit(100);
		testAccount.deposit(50);
		double actualBalance = testAccount.getBalance();
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 14. Balance should accurately reflect a series of deposits and withdrawals
	@Test
	void testBalanceAfterMixedTransactions() {
		double expectedBalance = 75;
		testAccount.deposit(200);
		testAccount.withdraw(50);
		testAccount.deposit(25);
		testAccount.withdraw(100);
		double actualBalance = testAccount.getBalance();
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 15. Balance should still be checkable and accurate after all funds are withdrawn
	@Test
	void testBalanceAfterFullWithdrawal() {
		double expectedBalance = 0;
		testAccount.deposit(500);
		testAccount.withdraw(500);
		double actualBalance = testAccount.getBalance();
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 16. A new account should not be closed
	@Test
	void testNewAccountIsNotClosed() {
		assertFalse(testAccount.isClosed());
	}

	// 17. Closing an account with zero balance should mark it as closed
	@Test
	void testCloseAccountWithZeroBalance() {
		testAccount.close();
		assertTrue(testAccount.isClosed());
	}

	// 18. Closing an already closed account should throw an exception
	@Test
	void testCloseAlreadyClosedAccount() {
		testAccount.close();
		assertThrows(IllegalStateException.class, () -> {
			testAccount.close();
		});
	}

	// 19. Depositing into a closed account should throw an exception
	@Test
	void testDepositIntoClosedAccount() {
		testAccount.close();
		assertThrows(IllegalStateException.class, () -> {
			testAccount.deposit(50);
		});
	}

	// 20. Withdrawing from a closed account should throw an exception
	@Test
	void testWithdrawFromClosedAccount() {
		testAccount.deposit(100);
		testAccount.withdraw(100);
		testAccount.close();
		assertThrows(IllegalStateException.class, () -> {
			testAccount.withdraw(10);
		});
	}

}
