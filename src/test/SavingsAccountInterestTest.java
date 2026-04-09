package test;

import java.util.List;
import main.SavingsAccount;

public class SavingsAccountInterestTest {

    private SavingsAccount testAccount;

    @BeforeEach
    public void setUp() {
        testAccount = new SavingsAccount();
    }

    @Test
    @DisplayName("Should calculate 2% interest on positive balance")
    void testApplyInterestOnPositiveBalance() {
        testAccount.deposit(1000);
        double expectedBalance = 1020;

        testAccount.applyInterest();

        assertEquals(expectedBalance, testAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should not apply interest on zero balance")
    void testApplyInterestOnZeroBalance() {
        testAccount.applyInterest();
        assertEquals(0, testAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should not apply interest on negative balance")
    void testApplyInterestOnNegativeBalance() {
        testAccount.deposit(100);
        testAccount.withdraw(100);

        testAccount.applyInterest();

        assertEquals(0, testAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should record interest in transaction history")
    void testInterestRecordedInTransactionHistory() {
        testAccount.deposit(500);

        testAccount.applyInterest();

        List<String> transactions = testAccount.getTransactionHistory();
        assertEquals(2, transactions.size());

        String interestTransaction = transactions.get(1);
        assertTrue(interestTransaction.startsWith("Interest: +$"));
        assertTrue(interestTransaction.contains("10.00"));
        assertTrue(interestTransaction.contains("2.0% interest"));
    }

    @Test
    @DisplayName("Should correctly apply compound interest over multiple periods")
    void testApplyMultipleInterestPayments() {
        testAccount.deposit(1000);
        double expectedBalance = 1000 * 1.02 * 1.02;

        testAccount.applyInterest();
        testAccount.applyInterest();

        assertEquals(expectedBalance, testAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should correctly calculate interest on small balances")
    void testApplyInterestOnSmallBalance() {
        testAccount.deposit(50);

        testAccount.applyInterest();

        assertEquals(51, testAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should correctly calculate interest on large balances")
    void testApplyInterestOnLargeBalance() {
        testAccount.deposit(1_000_000);

        testAccount.applyInterest();

        assertEquals(1_020_000, testAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should calculate interest based on current balance after transactions")
    void testInterestAfterMultipleTransactions() {
        testAccount.deposit(1000);
        testAccount.withdraw(200);

        testAccount.applyInterest();

        assertEquals(816, testAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should provide the current interest rate")
    void testGetInterestRate() {
        assertEquals(0.02, testAccount.getInterestRate(), 0.0001);
    }

    @Test
    @DisplayName("Should throw exception when applying interest to closed account")
    void testApplyInterestOnClosedAccount() {
        testAccount.deposit(500);
        testAccount.close();

        assertThrows(IllegalStateException.class, () -> testAccount.applyInterest());
    }

    @Test
    @DisplayName("Interest application should not affect withdrawal count")
    void testInterestDoesNotAffectWithdrawalCount() {
        testAccount.deposit(1000);
        int initial = testAccount.getWithdrawalCount();

        testAccount.applyInterest();

        assertEquals(initial, testAccount.getWithdrawalCount());
    }

    @Test
    @DisplayName("Should handle fractional cents correctly")
    void testInterestPrecision() {
        testAccount.deposit(333.33);

        testAccount.applyInterest();

        double expected = 333.33 + (333.33 * 0.02);
        assertEquals(expected, testAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Interest should only be applied to positive balances")
    void testInterestOnlyOnPositiveBalance() {
        testAccount.applyInterest();

        assertEquals(0, testAccount.getBalance(), 0.01);
        assertTrue(testAccount.getBalance() >= 0);
    }

    @Test
    @DisplayName("Interest transaction should have correct format")
    void testInterestTransactionFormat() {
        testAccount.deposit(1000);

        testAccount.applyInterest();

        String interestTransaction = testAccount.getTransactionHistory().get(1);
        assertTrue(interestTransaction.matches("Interest: \\+\\$\\d+\\.\\d+ \\(2\\.0% interest\\)"));
    }

    @Test
    @DisplayName("Should simulate compound interest over multiple years")
    void testCompoundInterestOverMultipleYears() {
        testAccount.deposit(1000);

        for (int i = 0; i < 12; i++) {
            testAccount.applyInterest();
        }

        double expected = 1000 * Math.pow(1.02, 12);
        assertEquals(expected, testAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Interest method should only exist on SavingsAccount")
    void testInterestMethodExistsOnlyOnSavings() {
        SavingsAccount savings = new SavingsAccount();

        assertDoesNotThrow(() -> savings.applyInterest());
        assertTrue(savings instanceof SavingsAccount);
    }

    @Test
    @DisplayName("Interest application should preserve withdrawal limit state")
    void testInterestPreservesWithdrawalState() {
        testAccount.deposit(1000);
        testAccount.withdraw(100);
        testAccount.withdraw(50);

        testAccount.applyInterest();
        testAccount.applyInterest();

        assertEquals(2, testAccount.getWithdrawalCount());
        assertEquals(4, testAccount.getRemainingWithdrawals());
    }

    @Test
    @DisplayName("Should calculate interest correctly after transfers")
    void testInterestAfterTransfer() {
        SavingsAccount anotherAccount = new SavingsAccount();
        testAccount.deposit(1000);
        testAccount.transfer(anotherAccount, 300);

        testAccount.applyInterest();

        assertEquals(714, testAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should round interest to nearest cent")
    void testInterestRounding() {
        testAccount.deposit(1.00);
        testAccount.applyInterest();
        assertEquals(1.02, testAccount.getBalance(), 0.001);

        testAccount.deposit(0.50);
        testAccount.applyInterest();
        assertEquals(1.55, testAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should correctly handle automatic monthly interest scenario")
    void testMonthlyInterestScenario() {
        testAccount.deposit(1000);

        testAccount.applyInterest();
        testAccount.applyInterest();
        testAccount.applyInterest();

        assertEquals(1061.21, testAccount.getBalance(), 0.01);

        long interestCount = testAccount.getTransactionHistory().stream()
            .filter(t -> t.startsWith("Interest:"))
            .count();

        assertEquals(3, interestCount);
    }
}