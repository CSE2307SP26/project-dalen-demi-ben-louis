package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.SavingsAccount;
import main.CheckingAccount;
import main.BankAccount;

class SavingsAccountTest {

	private SavingsAccount testAccount;

	@BeforeEach
	public void setUp() {
		testAccount = new SavingsAccount();
	}

	// 1. A savings account should report its type as "Savings"
	@Test
	void testSavingsAccountType() {
		// 1. Create necessary objects
		String expectedType = "Savings";
		// 2. Call the method being tested
		String actualType = testAccount.getAccountType();
		// 3. Use assertions to validate the result
		assertEquals(expectedType, actualType);
	}

	// 2. A new savings account should start with a balance of zero
	@Test
	void testSavingsAccountInitialBalance() {
		// 1. Create necessary objects
		double expectedBalance = 0;
		// 2. Call the method being tested
		double actualBalance = testAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 3. A savings account should support deposits
	@Test
	void testSavingsAccountDeposit() {
		// 1. Create necessary objects
		double expectedBalance = 250;
		// 2. Call the method being tested
		testAccount.deposit(250);
		double actualBalance = testAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 4. A savings account should support withdrawals
	@Test
	void testSavingsAccountWithdraw() {
		// 1. Create necessary objects
		double expectedBalance = 150;
		// 2. Call the methods being tested
		testAccount.deposit(200);
		testAccount.withdraw(50);
		double actualBalance = testAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 5. A savings account should be an instance of BankAccount (polymorphism)
	@Test
	void testSavingsAccountIsInstanceOfBankAccount() {
		// 1. Create necessary objects
		// (testAccount created in setUp)
		// 2. Call the method being tested
		boolean isInstance = testAccount instanceof BankAccount;
		// 3. Use assertions to validate the result
		assertTrue(isInstance);
	}

	// 6. A savings account should be able to transfer to a checking account
	@Test
	void testSavingsAccountTransferToChecking() {
		// 1. Create necessary objects
		CheckingAccount checkingAccount = new CheckingAccount();
		double expectedSavingsBalance = 80;
		double expectedCheckingBalance = 20;
		// 2. Call the methods being tested
		testAccount.deposit(100);
		testAccount.transfer(checkingAccount, 20);
		double actualSavingsBalance = testAccount.getBalance();
		double actualCheckingBalance = checkingAccount.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedSavingsBalance, actualSavingsBalance, 0.01);
		assertEquals(expectedCheckingBalance, actualCheckingBalance, 0.01);
	}

	// 7. A savings account should be able to transfer to another savings account
	@Test
	void testSavingsAccountTransferToSavings() {
		// 1. Create necessary objects
		SavingsAccount otherSavings = new SavingsAccount();
		double expectedSourceBalance = 70;
		double expectedTargetBalance = 30;
		// 2. Call the methods being tested
		testAccount.deposit(100);
		testAccount.transfer(otherSavings, 30);
		double actualSourceBalance = testAccount.getBalance();
		double actualTargetBalance = otherSavings.getBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedSourceBalance, actualSourceBalance, 0.01);
		assertEquals(expectedTargetBalance, actualTargetBalance, 0.01);
	}

	// 8. A savings account type should be different from a checking account type
	@Test
	void testSavingsAndCheckingAreDifferentTypes() {
		// 1. Create necessary objects
		CheckingAccount checkingAccount = new CheckingAccount();
		// 2. Call the methods being tested
		String savingsType = testAccount.getAccountType();
		String checkingType = checkingAccount.getAccountType();
		// 3. Use assertions to validate the result
		assertNotEquals(savingsType, checkingType);
	}

}
