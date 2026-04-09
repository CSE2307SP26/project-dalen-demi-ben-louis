package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import main.BankAccount;
import main.CheckingAccount;
import main.SavingsAccount;

class CheckingAccountTest {

	private CheckingAccount testAccount;

	@BeforeEach
	public void setUp() {
		testAccount = new CheckingAccount();
	}

	// 1. A checking account should report its type as "Checking"
	@Test
	void testCheckingAccountType() {
		// 1. Create necessary objects
		String expectedType = "Checking";
		// 2. Call the method being tested
		String actualType = testAccount.getAccountType();
		// 3. Use assertions to validate the result
		assertEquals(expectedType, actualType);
	}

	// 2. A new checking account should start with a balance of zero
	@Test
	void testCheckingAccountInitialBalance() {
		// 1. Create necessary objects
		double expectedBalance = 0;
		// 2. Call the method being tested
		double actualBalance = testAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 3. A checking account should support deposits
	@Test
	void testCheckingAccountDeposit() {
		// 1. Create necessary objects
		double expectedBalance = 100;
		// 2. Call the method being tested
		testAccount.deposit(100);
		double actualBalance = testAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 4. A checking account should support withdrawals
	@Test
	void testCheckingAccountWithdraw() {
		// 1. Create necessary objects
		double expectedBalance = 60;
		// 2. Call the methods being tested
		testAccount.deposit(100);
		testAccount.withdraw(40);
		double actualBalance = testAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 5. A checking account should be an instance of BankAccount (polymorphism)
	@Test
	void testCheckingAccountIsInstanceOfBankAccount() {
		// 1. Create necessary objects
		// (testAccount created in setUp)
		// 2. Call the method being tested
		boolean isInstance = testAccount instanceof BankAccount;
		// 3. Use assertions to validate the result
		assertTrue(isInstance);
	}

	// 6. A checking account should be able to transfer to a savings account
	@Test
	void testCheckingAccountTransferToSavings() {
		// 1. Create necessary objects
		SavingsAccount savingsAccount = new SavingsAccount();
		double expectedCheckingBalance = 60;
		double expectedSavingsBalance = 40;
		// 2. Call the methods being tested
		testAccount.deposit(100);
		testAccount.transfer(savingsAccount, 40);
		double actualCheckingBalance = testAccount.getBalance();
		double actualSavingsBalance = savingsAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedCheckingBalance, actualCheckingBalance, 0.01);
		assertEquals(expectedSavingsBalance, actualSavingsBalance, 0.01);
	}

	// 7. A checking account and a savings account should report different types
	@Test
	void testCheckingAndSavingsAreDifferentTypes() {
		// 1. Create necessary objects
		SavingsAccount savingsAccount = new SavingsAccount();
		// 2. Call the methods being tested
		String checkingType = testAccount.getAccountType();
		String savingsType = savingsAccount.getAccountType();
		// 3. Use assertions to validate the result
		assertNotEquals(checkingType, savingsType);
	}

	// 8. Withdrawing more than the balance but within the overdraft limit should succeed
	@Test
	void testOverdraftWithinLimit() {
		// 1. Create necessary objects
		double expectedBalance = -85; // 50 - 100 = -50, then -35 overdraft fee = -85
		// 2. Call the methods being tested
		testAccount.deposit(50);
		testAccount.withdraw(100);
		double actualBalance = testAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 9. An overdraft fee should appear in the transaction history
	@Test
	void testOverdraftFeeAppearsInTransactionHistory() {
		// 1. Create necessary objects
		String expectedFeeEntry = "Overdraft Fee: -$35.00";
		// 2. Call the methods being tested
		testAccount.deposit(50);
		testAccount.withdraw(100);
		List<String> transactions = testAccount.getTransactionHistory();
		// 3. Use assertions to validate the result
		assertTrue(transactions.contains(expectedFeeEntry));
	}

	// 10. Withdrawing beyond the overdraft limit should throw an exception
	@Test
	void testOverdraftExceedsLimitThrowsException() {
		// 1. Create necessary objects
		testAccount.deposit(50);
		// 2. Call the method being tested and 3. Use assertions to validate the result
		assertThrows(IllegalArgumentException.class, () -> {
			testAccount.withdraw(200); // 200 > 50 + 100 limit = 150
		});
	}

	// 11. Withdrawing exactly to the overdraft limit should succeed and incur the fee
	@Test
	void testOverdraftExactlyAtLimit() {
		// 1. Create necessary objects
		double expectedBalance = -135; // 0 - 100 = -100, then -35 overdraft fee = -135
		// 2. Call the methods being tested
		testAccount.withdraw(100); // balance is 0, overdraft limit is 100
		double actualBalance = testAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 12. A withdrawal that does not cause overdraft should not incur a fee
	@Test
	void testNoFeeWhenWithdrawalDoesNotCauseOverdraft() {
		// 1. Create necessary objects
		double expectedBalance = 50;
		int expectedTransactionCount = 2; // deposit + withdrawal only, no fee
		// 2. Call the methods being tested
		testAccount.deposit(100);
		testAccount.withdraw(50);
		double actualBalance = testAccount.getBalance();
		List<String> transactions = testAccount.getTransactionHistory();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
		assertEquals(expectedTransactionCount, transactions.size());
	}

	// 13. Balance should not change when an overdraft withdrawal is rejected
	@Test
	void testBalanceUnchangedAfterRejectedOverdraft() {
		// 1. Create necessary objects
		double expectedBalance = 50;
		testAccount.deposit(50);
		// 2. Call the method being tested
		try {
			testAccount.withdraw(200); // exceeds balance + overdraft limit
		} catch (IllegalArgumentException e) {
			// expected
		}
		double actualBalance = testAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 14. Multiple overdraft withdrawals should each incur their own fee
	@Test
	void testMultipleOverdraftsEachIncurFee() {
		// 1. Create necessary objects
		// deposit 100, withdraw 120: 100 - 120 = -20, fee: -20 - 35 = -55
		// withdraw 10: -55 - 10 = -65, fee: -65 - 35 = -100
		double expectedBalance = -100;
		// 2. Call the methods being tested
		testAccount.deposit(100);
		testAccount.withdraw(120);
		testAccount.withdraw(10);
		double actualBalance = testAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 15. The overdraft limit should be retrievable
	@Test
	void testGetOverdraftLimit() {
		// 1. Create necessary objects
		double expectedLimit = 100;
		// 2. Call the method being tested
		double actualLimit = testAccount.getOverdraftLimit();
		// 3. Use assertions to validate the result
		assertEquals(expectedLimit, actualLimit, 0.01);
	}

}
