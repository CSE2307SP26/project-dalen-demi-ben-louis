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

}
import main.BankAccount;

public class BankAccountTest {

    @Test
    public void testDeposit() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidDeposit() {
        BankAccount testAccount = new BankAccount();
        assertThrows(IllegalArgumentException.class, () -> testAccount.deposit(-50));
    }

    @Test
    public void testMultipleAccounts() {
        BankAccount firstAccount = new BankAccount();
        BankAccount secondAccount = new BankAccount();

        firstAccount.deposit(100);
        secondAccount.deposit(200);

        assertEquals(100, firstAccount.getBalance(), 0.01);
        assertEquals(200, secondAccount.getBalance(), 0.01);
    }

    @Test
    public void testAccountCreationAndIndependence() {
        // Create multiple accounts
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();
        BankAccount account3 = new BankAccount();
        
        // Initial balances should all be zero
        assertEquals(0, account1.getBalance(), 0.01);
        assertEquals(0, account2.getBalance(), 0.01);
        assertEquals(0, account3.getBalance(), 0.01);
        
        // Deposit different amounts in each account
        account1.deposit(150.50);
        account2.deposit(275.75);
        account3.deposit(89.99);
        
        // Verify each account maintains its own balance independently
        assertEquals(150.50, account1.getBalance(), 0.01);
        assertEquals(275.75, account2.getBalance(), 0.01);
        assertEquals(89.99, account3.getBalance(), 0.01);
        
        // Make additional deposits to specific accounts
        account1.deposit(49.50);  
        account3.deposit(10.01);  
        
        // Verify final balances are correct and accounts don't interfere with each other
        assertEquals(200.00, account1.getBalance(), 0.01);
        assertEquals(275.75, account2.getBalance(), 0.01);  
        assertEquals(100.00, account3.getBalance(), 0.01);
    }
}
