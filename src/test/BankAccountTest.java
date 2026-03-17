package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

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
            //do nothing, test passes
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
    
    //test to verify transaction history returns a copy (encapsulation)
    @Test
    public void testTransactionHistoryImmutability() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        
        List<String> transactions = testAccount.getTransactionHistory();
        assertEquals(1, transactions.size());
        
        // Try to modify the returned list
        transactions.clear();
        
        //Original should be unchanged
        List<String> transactions2 = testAccount.getTransactionHistory();
        
        assertEquals(1, transactions2.size());
    }
}
