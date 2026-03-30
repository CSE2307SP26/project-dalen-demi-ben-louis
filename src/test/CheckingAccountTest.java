package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.CheckingAccount;
import main.SavingsAccount;
import main.BankAccount;

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

}
