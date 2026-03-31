package test;

import main.BankAccount;
import main.CheckingAccount;
import main.SavingsAccount;

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
	// 9. Test that a savings account allows up to 6 withdrawals in a month
@Test
void testSavingsAccountAllowsUpToSixWithdrawals() {
    // 1. Create necessary objects
    double expectedBalance = 1000 - (6 * 10);
    // 2. Call the methods being tested
    testAccount.deposit(1000);
    for (int i = 0; i < 6; i++) {
        testAccount.withdraw(10);
    }
    double actualBalance = testAccount.getBalance();
    // 3. Use assertions to validate the result
    assertEquals(expectedBalance, actualBalance, 0.01);
}

// 10. Test that a savings account blocks the 7th withdrawal in a month
@Test
void testSavingsAccountBlocksSeventhWithdrawal() {
    // 1. Create necessary objects
    testAccount.deposit(1000);
    // Make 6 successful withdrawals
    for (int i = 0; i < 6; i++) {
        testAccount.withdraw(10);
    }
    double expectedBalance = 1000 - (6 * 10);
    // 2. Call the method being tested and 3. Use assertions
    assertThrows(IllegalStateException.class, () -> {
        testAccount.withdraw(10);
    });
    double actualBalance = testAccount.getBalance();
    assertEquals(expectedBalance, actualBalance, 0.01);
}

// 11. Test that withdrawals from savings account are counted correctly
@Test
void testSavingsAccountWithdrawalCount() {
    // 1. Create necessary objects
    // 2. Call the methods being tested
    testAccount.deposit(1000);
    testAccount.withdraw(100);
    testAccount.withdraw(50);
    testAccount.withdraw(25);
    int actualCount = testAccount.getWithdrawalCount();
    // 3. Use assertions to validate the result
    assertEquals(3, actualCount);
}

// 12. Test that transfers from savings account count toward withdrawal limit
@Test
void testTransferFromSavingsCountsAsWithdrawal() {
    // 1. Create necessary objects
    SavingsAccount targetAccount = new SavingsAccount();
    // 2. Call the methods being tested
    testAccount.deposit(500);
    testAccount.transfer(targetAccount, 100);
    int actualCount = testAccount.getWithdrawalCount();
    // 3. Use assertions to validate the result
    assertEquals(1, actualCount);
    assertEquals(400, testAccount.getBalance(), 0.01);
    assertEquals(100, targetAccount.getBalance(), 0.01);
}

// 13. Test that transfers are blocked when withdrawal limit is reached
@Test
void testTransferBlockedWhenWithdrawalLimitReached() {
    // 1. Create necessary objects
    SavingsAccount targetAccount = new SavingsAccount();
    // 2. Call the methods being tested
    testAccount.deposit(1000);
    // Make 6 withdrawals to reach limit
    for (int i = 0; i < 6; i++) {
        testAccount.withdraw(10);
    }
    double expectedSourceBalance = testAccount.getBalance();
    double expectedTargetBalance = targetAccount.getBalance();
    // 3. Use assertions to validate the result
    assertThrows(IllegalStateException.class, () -> {
        testAccount.transfer(targetAccount, 50);
    });
    assertEquals(expectedSourceBalance, testAccount.getBalance(), 0.01);
    assertEquals(expectedTargetBalance, targetAccount.getBalance(), 0.01);
}

// 14. Test that remaining withdrawals calculation is accurate
@Test
void testRemainingWithdrawalsCalculation() {
    // 1. Create necessary objects
    // 2. Call the methods being tested
    testAccount.deposit(1000);
    assertEquals(6, testAccount.getRemainingWithdrawals());
    testAccount.withdraw(100);
    assertEquals(5, testAccount.getRemainingWithdrawals());
    testAccount.withdraw(100);
    assertEquals(4, testAccount.getRemainingWithdrawals());
    testAccount.withdraw(100);
    assertEquals(3, testAccount.getRemainingWithdrawals());
    testAccount.withdraw(100);
    assertEquals(2, testAccount.getRemainingWithdrawals());
    testAccount.withdraw(100);
    assertEquals(1, testAccount.getRemainingWithdrawals());
    testAccount.withdraw(100);
    assertEquals(0, testAccount.getRemainingWithdrawals());
    // 3. Use assertions - all asserts are in the method calls above
}

// 15. Test that failed withdrawals don't count toward the limit
@Test
void testFailedWithdrawalsDoNotCountTowardLimit() {
    // 1. Create necessary objects
    // 2. Call the methods being tested
    testAccount.deposit(100);
    assertThrows(IllegalArgumentException.class, () -> {
        testAccount.withdraw(200); // Insufficient funds
    });
    int actualCount = testAccount.getWithdrawalCount();
    // 3. Use assertions to validate the result
    assertEquals(0, actualCount);
}

// 16. Test that deposits don't affect the withdrawal count
@Test
void testDepositsDoNotAffectWithdrawalCount() {
    // 1. Create necessary objects
    // 2. Call the methods being tested
    testAccount.deposit(100);
    testAccount.withdraw(50);
    testAccount.deposit(200);
    testAccount.withdraw(30);
    int actualCount = testAccount.getWithdrawalCount();
    // 3. Use assertions to validate the result
    assertEquals(2, actualCount);
    assertEquals(220, testAccount.getBalance(), 0.01); // 100 - 50 + 200 - 30 = 220
}

// 17. Test that checking accounts are not subject to withdrawal limit
@Test
void testCheckingAccountNotAffectedByWithdrawalLimit() {
    // 1. Create necessary objects
    CheckingAccount checkingAccount = new CheckingAccount();
    // 2. Call the methods being tested
    checkingAccount.deposit(1000);
    // Make 10 withdrawals (exceeds savings limit)
    for (int i = 0; i < 10; i++) {
        checkingAccount.withdraw(10);
    }
    double expectedBalance = 900; // 1000 - (10 * 10) = 900
    double actualBalance = checkingAccount.getBalance();
    // 3. Use assertions to validate the result
    assertEquals(expectedBalance, actualBalance, 0.01);
    // Verify no IllegalStateException was thrown
}

// 18. Test that withdrawal limit error message is correct
@Test
void testWithdrawalLimitErrorMessage() {
    // 1. Create necessary objects
    testAccount.deposit(1000);
    // Make 6 withdrawals
    for (int i = 0; i < 6; i++) {
        testAccount.withdraw(10);
    }
    // 2. Call the method being tested and 3. Use assertions
    Exception exception = assertThrows(IllegalStateException.class, () -> {
        testAccount.withdraw(10);
    });
    String expectedMessage = "Monthly withdrawal limit exceeded. Maximum 6 withdrawals per month.";
    assertEquals(expectedMessage, exception.getMessage());
}

// 19. Test that withdrawal count is zero for new account
@Test
void testNewSavingsAccountWithdrawalCountIsZero() {
    // 1. Create necessary objects
    // 2. Call the method being tested
    int actualCount = testAccount.getWithdrawalCount();
    // 3. Use assertions to validate the result
    assertEquals(0, actualCount);
}

// 20. Test that remaining withdrawals is 6 for new account
@Test
void testNewSavingsAccountRemainingWithdrawalsIsSix() {
    // 1. Create necessary objects
    // 2. Call the method being tested
    int actualRemaining = testAccount.getRemainingWithdrawals();
    // 3. Use assertions to validate the result
    assertEquals(6, actualRemaining);
}

}
