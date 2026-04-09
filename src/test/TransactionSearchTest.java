package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import main.BankAccount;
import main.CheckingAccount;
import main.SavingsAccount;

public class TransactionSearchTest {

    private BankAccount testAccount;

    @BeforeEach
    public void setUp() {
        testAccount = new BankAccount();
    }

    // 1. Test searching for deposits
    @Test
    @DisplayName("Should find all deposit transactions")
    void testSearchForDeposits() {
        // Arrange
        testAccount.deposit(100);
        testAccount.deposit(50);
        testAccount.withdraw(30);
        testAccount.deposit(200);
        
        // Act
        List<String> results = testAccount.searchTransactions("Deposit");
        
        // Assert
        assertEquals(3, results.size());
        assertTrue(results.get(0).contains("Deposit: +$100.00"));
        assertTrue(results.get(1).contains("Deposit: +$50.00"));
        assertTrue(results.get(2).contains("Deposit: +$200.00"));
    }

    // 2. Test searching for withdrawals
    @Test
    @DisplayName("Should find all withdrawal transactions")
    void testSearchForWithdrawals() {
        // Arrange
        testAccount.deposit(500);
        testAccount.withdraw(100);
        testAccount.withdraw(50);
        testAccount.deposit(200);
        
        // Act
        List<String> results = testAccount.searchTransactions("Withdrawal");
        
        // Assert
        assertEquals(2, results.size());
        assertTrue(results.get(0).contains("Withdrawal: -$100.00"));
        assertTrue(results.get(1).contains("Withdrawal: -$50.00"));
    }

    // 3. Test searching for transfers
    @Test
    @DisplayName("Should find transfer transactions")
    void testSearchForTransfers() {
        // Arrange
        BankAccount targetAccount = new BankAccount();
        testAccount.deposit(500);
        testAccount.transfer(targetAccount, 100);
        testAccount.transfer(targetAccount, 50);
        
        // Act
        List<String> results = testAccount.searchTransactions("Transfer");
        
        // Assert
        assertEquals(2, results.size());
        assertTrue(results.get(0).contains("Transfer Out: -$100.00"));
        assertTrue(results.get(1).contains("Transfer Out: -$50.00"));
    }

    // 4. Test searching for loans
    @Test
    @DisplayName("Should find loan transactions")
    void testSearchForLoans() {
        // Arrange
        testAccount.deposit(200);
        testAccount.takeLoan(50);
        testAccount.takeLoan(30);
        
        // Act
        List<String> results = testAccount.searchTransactions("Loan");
        
        // Assert
        assertEquals(2, results.size());
        assertTrue(results.get(0).contains("Loan: +$50.00"));
        assertTrue(results.get(1).contains("Loan: +$30.00"));
    }

    // 5. Test case-insensitive search
    @Test
    @DisplayName("Should be case-insensitive when searching")
    void testCaseInsensitiveSearch() {
        // Arrange
        testAccount.deposit(100);
        testAccount.withdraw(50);
        
        // Act
        List<String> lowerResults = testAccount.searchTransactions("deposit");
        List<String> upperResults = testAccount.searchTransactions("DEPOSIT");
        List<String> mixedResults = testAccount.searchTransactions("DePoSiT");
        
        // Assert
        assertEquals(1, lowerResults.size());
        assertEquals(1, upperResults.size());
        assertEquals(1, mixedResults.size());
        assertTrue(lowerResults.get(0).contains("Deposit: +$100.00"));
    }

    // 6. Test searching with partial keyword
    @Test
    @DisplayName("Should find transactions with partial keyword matches")
    void testPartialKeywordSearch() {
        // Arrange
        testAccount.deposit(100);
        testAccount.withdraw(50);
        testAccount.transfer(new BankAccount(), 25);
        
        // Act
        List<String> results = testAccount.searchTransactions("with");
        
        // Assert
        assertEquals(1, results.size());
        assertTrue(results.get(0).contains("Withdrawal"));
    }

    // 7. Test search with no matches
    @Test
    @DisplayName("Should return empty list when no transactions match")
    void testSearchNoMatches() {
        // Arrange
        testAccount.deposit(100);
        testAccount.withdraw(50);
        
        // Act
        List<String> results = testAccount.searchTransactions("xyzabc");
        
        // Assert
        assertTrue(results.isEmpty());
    }

    // 8. Test search on empty transaction history
    @Test
    @DisplayName("Should return empty list for account with no transactions")
    void testSearchEmptyTransactionHistory() {
        // Act
        List<String> results = testAccount.searchTransactions("Deposit");
        
        // Assert
        assertTrue(results.isEmpty());
    }

    // 9. Test search with null keyword throws exception
    @Test
    @DisplayName("Should throw exception when keyword is null")
    void testSearchWithNullKeyword() {
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.searchTransactions(null);
        });
    }

    // 10. Test search with empty string throws exception
    @Test
    @DisplayName("Should throw exception when keyword is empty")
    void testSearchWithEmptyKeyword() {
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.searchTransactions("");
        });
    }

    // 11. Test search with whitespace-only keyword throws exception
    @Test
    @DisplayName("Should throw exception when keyword is only whitespace")
    void testSearchWithWhitespaceKeyword() {
        assertThrows(IllegalArgumentException.class, () -> {
            testAccount.searchTransactions("   ");
        });
    }

    // 12. Test searching for specific amounts
    @Test
    @DisplayName("Should find transactions by amount")
    void testSearchByAmount() {
        // Arrange
        testAccount.deposit(100);
        testAccount.withdraw(50);
        testAccount.deposit(75);
        testAccount.withdraw(100);
        
        // Act
        List<String> results = testAccount.searchTransactions("100");
        
        // Assert
        assertEquals(2, results.size());
        assertTrue(results.get(0).contains("Deposit: +$100.00"));
        assertTrue(results.get(1).contains("Withdrawal: -$100.00"));
    }

    // 13. Test search on CheckingAccount
    @Test
    @DisplayName("Should search transactions in CheckingAccount")
    void testSearchOnCheckingAccount() {
        // Arrange
        CheckingAccount checking = new CheckingAccount();
        checking.deposit(500);
        checking.withdraw(100);
        checking.withdraw(200);
        
        // Act
        List<String> results = checking.searchTransactions("Withdrawal");
        
        // Assert
        assertEquals(2, results.size());
        assertTrue(results.get(0).contains("Withdrawal: -$100.00"));
        assertTrue(results.get(1).contains("Withdrawal: -$200.00"));
    }

    // 14. Test search on SavingsAccount
    @Test
    @DisplayName("Should search transactions in SavingsAccount")
    void testSearchOnSavingsAccount() {
        // Arrange
        SavingsAccount savings = new SavingsAccount();
        savings.deposit(1000);
        savings.withdraw(100);
        savings.applyInterest();
        
        // Act
        List<String> depositResults = savings.searchTransactions("Deposit");
        List<String> interestResults = savings.searchTransactions("Interest");
        
        // Assert
        assertEquals(1, depositResults.size());
        assertEquals(1, interestResults.size());
        assertTrue(interestResults.get(0).contains("Interest: +$"));
    }

    // 15. Test multiple keyword searches
    @Test
    @DisplayName("Should handle multiple searches correctly")
    void testMultipleSearches() {
        // Arrange
        testAccount.deposit(100);
        testAccount.withdraw(50);
        testAccount.deposit(75);
        testAccount.withdraw(30);
        
        // Act
        List<String> depositResults = testAccount.searchTransactions("Deposit");
        List<String> withdrawalResults = testAccount.searchTransactions("Withdrawal");
        
        // Assert
        assertEquals(2, depositResults.size());
        assertEquals(2, withdrawalResults.size());
    }

    // 16. Test search after many transactions
    @Test
    @DisplayName("Should correctly search through many transactions")
    void testSearchWithManyTransactions() {
        // Arrange
        for (int i = 0; i < 50; i++) {
            testAccount.deposit(100);
            if (i % 2 == 0) {
                testAccount.withdraw(50);
            }
        }
        
        // Act
        List<String> depositResults = testAccount.searchTransactions("Deposit");
        List<String> withdrawalResults = testAccount.searchTransactions("Withdrawal");
        
        // Assert
        assertEquals(50, depositResults.size());
        assertEquals(25, withdrawalResults.size());
    }

    // 17. Test search for transfer in transactions
    @Test
    @DisplayName("Should find transfer in transactions")
    void testSearchForTransferIn() {
        // Arrange
        BankAccount sourceAccount = new BankAccount();
        sourceAccount.deposit(500);
        sourceAccount.transfer(testAccount, 100);
        
        // Act
        List<String> results = testAccount.searchTransactions("Transfer In");
        
        // Assert
        assertEquals(1, results.size());
        assertTrue(results.get(0).contains("Transfer In: +$100.00"));
    }

    // 18. Test search for overdraft fees
    @Test
    @DisplayName("Should find overdraft fee transactions in CheckingAccount")
    void testSearchForOverdraftFees() {
        // Arrange
        CheckingAccount checking = new CheckingAccount();
        checking.deposit(50);
        checking.withdraw(100); // Triggers overdraft fee
        
        // Act
        List<String> results = checking.searchTransactions("Overdraft");
        
        // Assert
        assertEquals(1, results.size());
        assertTrue(results.get(0).contains("Overdraft Fee: -$35.00"));
    }

    // 19. Test that original transaction list is unchanged
    @Test
    @DisplayName("Search should not modify original transaction list")
    void testSearchDoesNotModifyTransactions() {
        // Arrange
        testAccount.deposit(100);
        testAccount.withdraw(50);
        List<String> originalTransactions = testAccount.getTransactionHistory();
        int originalSize = originalTransactions.size();
        
        // Act
        List<String> results = testAccount.searchTransactions("Deposit");
        
        // Assert
        assertEquals(originalSize, testAccount.getTransactionHistory().size());
        assertEquals(originalTransactions, testAccount.getTransactionHistory());
    }

    // 20. Test search with special characters
    @Test
    @DisplayName("Should handle searches with special characters")
    void testSearchWithSpecialCharacters() {
        // Arrange
        testAccount.deposit(100);
        testAccount.withdraw(50);
        
        // Act & Assert - Should not throw exception
        List<String> results = testAccount.searchTransactions("$");
        // Dollar sign appears in all transactions, so should find both
        assertEquals(2, results.size());
    }
}