package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import main.AccountSummaryGenerator;
import main.BankAccount;
import main.CheckingAccount;
import main.SavingsAccount;

class AccountSummaryGeneratorTest {

	private List<BankAccount> accounts;
	private AccountSummaryGenerator generator;

	@BeforeEach
	public void setUp() {
		accounts = new ArrayList<>();
		generator = new AccountSummaryGenerator(accounts);
	}

	// 1. Total balance with no accounts should be zero
	@Test
	void testTotalBalanceWithNoAccounts() {
		// 1. Create necessary objects
		double expectedBalance = 0;
		// 2. Call the method being tested
		double actualBalance = generator.getTotalBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 2. Total balance should sum across multiple accounts
	@Test
	void testTotalBalanceWithMultipleAccounts() {
		// 1. Create necessary objects
		double expectedBalance = 1500; // 500 + 1000
		CheckingAccount checking = new CheckingAccount();
		checking.deposit(500);
		SavingsAccount savings = new SavingsAccount();
		savings.deposit(1000);
		accounts.add(checking);
		accounts.add(savings);
		// 2. Call the method being tested
		double actualBalance = generator.getTotalBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 3. Total balance should exclude closed accounts
	@Test
	void testTotalBalanceExcludesClosedAccounts() {
		// 1. Create necessary objects
		double expectedBalance = 1000;
		CheckingAccount checking = new CheckingAccount();
		checking.deposit(500);
		checking.close();
		SavingsAccount savings = new SavingsAccount();
		savings.deposit(1000);
		accounts.add(checking);
		accounts.add(savings);
		// 2. Call the method being tested
		double actualBalance = generator.getTotalBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 4. Total balance should reflect an overdrawn checking account
	@Test
	void testTotalBalanceWithOverdrawnChecking() {
		// 1. Create necessary objects
		// Checking: deposit 50, withdraw 100 → -50 - 35 fee = -85
		// Savings: deposit 1000
		// Total: -85 + 1000 = 915
		double expectedBalance = 915;
		CheckingAccount checking = new CheckingAccount();
		checking.deposit(50);
		checking.withdraw(100);
		SavingsAccount savings = new SavingsAccount();
		savings.deposit(1000);
		accounts.add(checking);
		accounts.add(savings);
		// 2. Call the method being tested
		double actualBalance = generator.getTotalBalance();
		// 3. Use assertions to validate the result
		assertEquals(expectedBalance, actualBalance, 0.01);
	}

	// 5. Open account count should only count non-closed accounts
	@Test
	void testOpenAccountCount() {
		// 1. Create necessary objects
		int expectedCount = 1;
		CheckingAccount checking = new CheckingAccount();
		checking.close();
		SavingsAccount savings = new SavingsAccount();
		accounts.add(checking);
		accounts.add(savings);
		// 2. Call the method being tested
		int actualCount = generator.getOpenAccountCount();
		// 3. Use assertions to validate the result
		assertEquals(expectedCount, actualCount);
	}

	// 6. Closed account count should only count closed accounts
	@Test
	void testClosedAccountCount() {
		// 1. Create necessary objects
		int expectedCount = 2;
		CheckingAccount checking = new CheckingAccount();
		checking.close();
		SavingsAccount savings = new SavingsAccount();
		savings.close();
		BankAccount standard = new BankAccount();
		accounts.add(checking);
		accounts.add(savings);
		accounts.add(standard);
		// 2. Call the method being tested
		int actualCount = generator.getClosedAccountCount();
		// 3. Use assertions to validate the result
		assertEquals(expectedCount, actualCount);
	}

	// 7. Total account count should include all accounts
	@Test
	void testTotalAccountCount() {
		// 1. Create necessary objects
		int expectedCount = 3;
		accounts.add(new CheckingAccount());
		accounts.add(new SavingsAccount());
		accounts.add(new BankAccount());
		// 2. Call the method being tested
		int actualCount = generator.getTotalAccountCount();
		// 3. Use assertions to validate the result
		assertEquals(expectedCount, actualCount);
	}

	// 8. Total deposits should sum deposits across all accounts
	@Test
	void testTotalDepositsAcrossAccounts() {
		// 1. Create necessary objects
		double expectedDeposits = 1500; // 500 + 1000
		CheckingAccount checking = new CheckingAccount();
		checking.deposit(500);
		SavingsAccount savings = new SavingsAccount();
		savings.deposit(1000);
		accounts.add(checking);
		accounts.add(savings);
		// 2. Call the method being tested
		double actualDeposits = generator.getTotalDeposits();
		// 3. Use assertions to validate the result
		assertEquals(expectedDeposits, actualDeposits, 0.01);
	}

	// 9. Total withdrawals should sum withdrawals across all accounts
	@Test
	void testTotalWithdrawalsAcrossAccounts() {
		// 1. Create necessary objects
		double expectedWithdrawals = 150; // 50 + 100
		CheckingAccount checking = new CheckingAccount();
		checking.deposit(500);
		checking.withdraw(50);
		SavingsAccount savings = new SavingsAccount();
		savings.deposit(1000);
		savings.withdraw(100);
		accounts.add(checking);
		accounts.add(savings);
		// 2. Call the method being tested
		double actualWithdrawals = generator.getTotalWithdrawals();
		// 3. Use assertions to validate the result
		assertEquals(expectedWithdrawals, actualWithdrawals, 0.01);
	}

	// 10. Totals should all be zero for an empty account list
	@Test
	void testAllTotalsZeroForEmptyList() {
		// 1. Create necessary objects
		// (accounts list is empty from setUp)
		// 2. Call the methods being tested
		double totalBalance = generator.getTotalBalance();
		int totalCount = generator.getTotalAccountCount();
		int openCount = generator.getOpenAccountCount();
		int closedCount = generator.getClosedAccountCount();
		double totalDeposits = generator.getTotalDeposits();
		double totalWithdrawals = generator.getTotalWithdrawals();
		// 3. Use assertions to validate the result
		assertEquals(0, totalBalance, 0.01);
		assertEquals(0, totalCount);
		assertEquals(0, openCount);
		assertEquals(0, closedCount);
		assertEquals(0, totalDeposits, 0.01);
		assertEquals(0, totalWithdrawals, 0.01);
	}

}
