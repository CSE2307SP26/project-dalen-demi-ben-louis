package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class FileManagerTest {
    
    private List<BankAccount> accounts;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        accounts = new ArrayList<>();
    }
    
    // 1. Test saving empty accounts to text file
    @Test
    void testSaveEmptyAccountsToTextFile() throws IOException {
        // 1. Create necessary objects
        String filename = tempDir.resolve("empty_accounts.txt").toString();
        
        // 2. Call the method being tested
        FileManager.saveAccountsToFile(accounts, filename);
        
        // 3. Use assertions to validate the result
        File file = new File(filename);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        
        // Verify content
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String firstLine = reader.readLine();
            assertEquals("=== BANK ACCOUNTS SNAPSHOT ===", firstLine);
        }
    }
    
    // 2. Test saving empty accounts to CSV file
    @Test
    void testSaveEmptyAccountsToCSVFile() throws IOException {
        // 1. Create necessary objects
        String filename = tempDir.resolve("empty_accounts.csv").toString();
        
        // 2. Call the method being tested
        FileManager.saveAccountsToCSV(accounts, filename);
        
        // 3. Use assertions to validate the result
        File file = new File(filename);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        
        // Verify header
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            assertEquals("Account Number,Account Type,Status,Balance,Transaction Count", header);
        }
    }
    
    // 3. Test saving a single checking account
    @Test
    void testSaveSingleCheckingAccount() throws IOException {
        // 1. Create necessary objects
        CheckingAccount checking = new CheckingAccount();
        checking.deposit(1000);
        checking.withdraw(50);
        accounts.add(checking);
        String filename = tempDir.resolve("checking_account.txt").toString();
        
        // 2. Call the method being tested
        FileManager.saveAccountsToFile(accounts, filename);
        
        // 3. Use assertions to validate the result
        File file = new File(filename);
        assertTrue(file.exists());
        
        // Verify content contains checking account info
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String content;
            boolean foundChecking = false;
            while ((content = reader.readLine()) != null) {
                if (content.contains("Type: Checking")) {
                    foundChecking = true;
                    break;
                }
            }
            assertTrue(foundChecking);
        }
    }
    
    // 4. Test saving a single savings account
    @Test
    void testSaveSingleSavingsAccount() throws IOException {
        // 1. Create necessary objects
        SavingsAccount savings = new SavingsAccount();
        savings.deposit(500);
        savings.withdraw(100);
        accounts.add(savings);
        String filename = tempDir.resolve("savings_account.txt").toString();
        
        // 2. Call the method being tested
        FileManager.saveAccountsToFile(accounts, filename);
        
        // 3. Use assertions to validate the result
        File file = new File(filename);
        assertTrue(file.exists());
        
        // Verify content contains savings account info
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String content;
            boolean foundSavings = false;
            boolean foundWithdrawals = false;
            while ((content = reader.readLine()) != null) {
                if (content.contains("Type: Savings")) {
                    foundSavings = true;
                }
                if (content.contains("Withdrawals Used This Month: 1")) {
                    foundWithdrawals = true;
                }
            }
            assertTrue(foundSavings);
            assertTrue(foundWithdrawals);
        }
    }
    
    // 5. Test saving multiple accounts of different types
    @Test
    void testSaveMultipleAccounts() throws IOException {
        // 1. Create necessary objects
        CheckingAccount checking = new CheckingAccount();
        checking.deposit(1000);
        
        SavingsAccount savings = new SavingsAccount();
        savings.deposit(2000);
        savings.withdraw(100);
        
        BankAccount standard = new BankAccount();
        standard.deposit(500);
        
        accounts.add(checking);
        accounts.add(savings);
        accounts.add(standard);
        
        String filename = tempDir.resolve("multiple_accounts.txt").toString();
        
        // 2. Call the method being tested
        FileManager.saveAccountsToFile(accounts, filename);
        
        // 3. Use assertions to validate the result
        File file = new File(filename);
        assertTrue(file.exists());
        
        // Count accounts in file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int accountCount = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--- Account")) {
                    accountCount++;
                }
            }
            assertEquals(3, accountCount);
        }
    }
    
    // 6. Test saving accounts with transaction history
    @Test
    void testSaveAccountsWithTransactions() throws IOException {
        // 1. Create necessary objects
        SavingsAccount savings = new SavingsAccount();
        savings.deposit(1000);
        savings.withdraw(100);
        savings.withdraw(50);
        savings.deposit(200);
        
        accounts.add(savings);
        String filename = tempDir.resolve("accounts_with_transactions.txt").toString();
        
        // 2. Call the method being tested
        FileManager.saveAccountsToFile(accounts, filename);
        
        // 3. Use assertions to validate the result
        File file = new File(filename);
        assertTrue(file.exists());
        
        // Verify transactions are saved
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int transactionCount = 0;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Deposit:") || line.contains("Withdrawal:")) {
                    transactionCount++;
                }
            }
            assertEquals(4, transactionCount); // deposit, withdraw, withdraw, deposit
        }
    }
    
    // 7. Test CSV format for multiple accounts
    @Test
    void testCSVFormatWithMultipleAccounts() throws IOException {
        // 1. Create necessary objects
        CheckingAccount checking = new CheckingAccount();
        checking.deposit(1000);
        
        SavingsAccount savings = new SavingsAccount();
        savings.deposit(2000);
        
        accounts.add(checking);
        accounts.add(savings);
        
        String filename = tempDir.resolve("accounts.csv").toString();
        
        // 2. Call the method being tested
        FileManager.saveAccountsToCSV(accounts, filename);
        
        // 3. Use assertions to validate the result
        File file = new File(filename);
        assertTrue(file.exists());
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read header
            String header = reader.readLine();
            assertEquals("Account Number,Account Type,Status,Balance,Transaction Count", header);
            
            // Read first account
            String firstAccount = reader.readLine();
            assertNotNull(firstAccount);
            assertTrue(firstAccount.contains("Checking"));
            assertTrue(firstAccount.contains("OPEN"));
            
            // Read second account
            String secondAccount = reader.readLine();
            assertNotNull(secondAccount);
            assertTrue(secondAccount.contains("Savings"));
            
            // Check summary section
            String summaryLine = reader.readLine();
            assertEquals("", summaryLine); // Empty line
            
            String summaryHeader = reader.readLine();
            assertEquals("\"=== SUMMARY ===\"", summaryHeader);
        }
    }
    
    // 8. Test saving closed accounts
    @Test
    void testSaveClosedAccounts() throws IOException {
        // 1. Create necessary objects
        SavingsAccount savings = new SavingsAccount();
        savings.deposit(1000);
        savings.withdraw(100);
        savings.close();
        
        accounts.add(savings);
        String filename = tempDir.resolve("closed_account.txt").toString();
        
        // 2. Call the method being tested
        FileManager.saveAccountsToFile(accounts, filename);
        
        // 3. Use assertions to validate the result
        File file = new File(filename);
        assertTrue(file.exists());
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean foundClosed = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Status: CLOSED")) {
                    foundClosed = true;
                    break;
                }
            }
            assertTrue(foundClosed);
        }
    }
    
    // 9. Test CSV summary totals calculation
    @Test
    void testCSVSummaryTotals() throws IOException {
        // 1. Create necessary objects
        CheckingAccount checking = new CheckingAccount();
        checking.deposit(1000);
        
        SavingsAccount savings = new SavingsAccount();
        savings.deposit(2000);
        
        BankAccount standard = new BankAccount();
        standard.deposit(500);
        
        accounts.add(checking);
        accounts.add(savings);
        accounts.add(standard);
        
        String filename = tempDir.resolve("accounts_with_summary.csv").toString();
        
        // 2. Call the method being tested
        FileManager.saveAccountsToCSV(accounts, filename);
        
        // 3. Use assertions to validate the result
        File file = new File(filename);
        assertTrue(file.exists());
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Skip header and accounts
            reader.readLine(); // header
            reader.readLine(); // account 1
            reader.readLine(); // account 2
            reader.readLine(); // account 3
            reader.readLine(); // empty line
            reader.readLine(); // summary header
            
            // Read total accounts line
            String totalAccountsLine = reader.readLine();
            assertTrue(totalAccountsLine.contains("Total Accounts"));
            assertTrue(totalAccountsLine.contains("3"));
            
            // Read total balance line
            String totalBalanceLine = reader.readLine();
            assertTrue(totalBalanceLine.contains("Total Balance"));
            assertTrue(totalBalanceLine.contains("3500.00")); // 1000 + 2000 + 500 = 3500
        }
    }
    
    // 10. Test file creation with timestamp
    @Test
    void testFileCreationWithTimestamp() throws IOException {
        // 1. Create necessary objects
        accounts.add(new CheckingAccount());
        String filename = tempDir.resolve("test.txt").toString();
        
        // 2. Call the method being tested
        FileManager.saveAccountsToFile(accounts, filename);
        
        // 3. Use assertions to validate the result
        File file = new File(filename);
        assertTrue(file.exists());
        assertTrue(file.lastModified() > 0);
        
        // Verify timestamp exists in file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // First line is header
            String timestampLine = reader.readLine(); // Second line is timestamp
            assertTrue(timestampLine.contains("Generated:"));
        }
    }
}