package test;

import java.util.List;
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
        try {
            testAccount.deposit(-50);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    // test for transaction history - empty initially
    @Test
    public void testEmptyTransactionHistory() {
        BankAccount testAccount = new BankAccount();
        List<String> transactions = testAccount.getTransactionHistory();
        assertTrue(transactions.isEmpty());
    }

    // test for transaction history after deposits
    @Test
    public void testTransactionHistoryAfterDeposits() {
        BankAccount testAccount = new BankAccount();
        
        testAccount.deposit(50);
        testAccount.deposit(75.50);
        
        List<String> transactions = testAccount.getTransactionHistory();
        
        assertEquals(2, transactions.size());
        assertEquals("Deposit: +$50.00", transactions.get(0));
        assertEquals("Deposit: +$75.50", transactions.get(1));
        assertEquals(125.50, testAccount.getBalance(), 0.01);
    }

    // test to verify transaction history returns a copy (encapsulation)
    @Test
    public void testTransactionHistoryImmutability() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        
        List<String> transactions = testAccount.getTransactionHistory();
        assertEquals(1, transactions.size());
        
        // Try to modify the returned list
        transactions.clear();
        
        // Original should be unchanged
        List<String> transactions2 = testAccount.getTransactionHistory();
        
        assertEquals(1, transactions2.size());
    }
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