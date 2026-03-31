package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileManager {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Saves all accounts and their transactions to a file
     * @param accounts List of bank accounts to save
     * @param filename Name of the file to save to
     * @throws IOException if an I/O error occurs
     */
    public static void saveAccountsToFile(List<BankAccount> accounts, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write header with timestamp
            writer.write("=== BANK ACCOUNTS SNAPSHOT ===");
            writer.newLine();
            writer.write("Generated: " + LocalDateTime.now().format(DATE_FORMATTER));
            writer.newLine();
            writer.write("Total Accounts: " + accounts.size());
            writer.newLine();
            writer.write("================================");
            writer.newLine();
            writer.newLine();
            
            // Write each account
            for (int i = 0; i < accounts.size(); i++) {
                BankAccount account = accounts.get(i);
                writeAccount(writer, account, i + 1);
            }
        }
    }
    
    /**
     * Saves accounts to a CSV file
     * @param accounts List of bank accounts to save
     * @param filename Name of the CSV file to save to
     * @throws IOException if an I/O error occurs
     */
    public static void saveAccountsToCSV(List<BankAccount> accounts, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write CSV header
            writer.write("Account Number,Account Type,Status,Balance,Transaction Count");
            writer.newLine();
            
            // Write each account
            for (int i = 0; i < accounts.size(); i++) {
                BankAccount account = accounts.get(i);
                String status = account.isClosed() ? "CLOSED" : "OPEN";
                String type = account.getAccountType();
                
                // Add specific info for different account types
                if (account instanceof SavingsAccount) {
                    SavingsAccount savings = (SavingsAccount) account;
                    type = "Savings (Remaining Withdrawals: " + savings.getRemainingWithdrawals() + ")";
                } else if (account instanceof CheckingAccount) {
                    CheckingAccount checking = (CheckingAccount) account;
                    type = "Checking (Overdraft Limit: $" + String.format("%.2f", checking.getOverdraftLimit()) + ")";
                }
                
                writer.write(String.format("%d,\"%s\",%s,$%.2f,%d",
                    (i + 1),
                    type,
                    status,
                    account.getBalance(),
                    account.getTransactionHistory().size()
                ));
                writer.newLine();
            }
            
            // Add summary section
            writer.newLine();
            writer.write("\"=== SUMMARY ===\"");
            writer.newLine();
            writer.write("\"Total Accounts:\"," + accounts.size());
            writer.newLine();
            
            long openAccounts = accounts.stream().filter(a -> !a.isClosed()).count();
            writer.write("\"Open Accounts:\"," + openAccounts);
            writer.newLine();
            writer.write("\"Closed Accounts:\"," + (accounts.size() - openAccounts));
            writer.newLine();
            
            double totalBalance = accounts.stream().filter(a -> !a.isClosed()).mapToDouble(BankAccount::getBalance).sum();
            writer.write("\"Total Balance (Open Accounts):\",$" + String.format("%.2f", totalBalance));
            writer.newLine();
        }
    }
    
    private static void writeAccount(BufferedWriter writer, BankAccount account, int accountNumber) throws IOException {
        writer.write("--- Account " + accountNumber + " ---");
        writer.newLine();
        writer.write("Type: " + account.getAccountType());
        writer.newLine();
        writer.write("Status: " + (account.isClosed() ? "CLOSED" : "OPEN"));
        writer.newLine();
        writer.write("Current Balance: $" + String.format("%.2f", account.getBalance()));
        writer.newLine();
        
        // Add account-specific information
        if (account instanceof SavingsAccount) {
            SavingsAccount savings = (SavingsAccount) account;
            writer.write("Withdrawals Used This Month: " + savings.getWithdrawalCount());
            writer.newLine();
            writer.write("Remaining Withdrawals: " + savings.getRemainingWithdrawals());
            writer.newLine();
        } else if (account instanceof CheckingAccount) {
            CheckingAccount checking = (CheckingAccount) account;
            writer.write("Overdraft Limit: $" + String.format("%.2f", checking.getOverdraftLimit()));
            writer.newLine();
        }
        
        // Write transaction history
        List<String> transactions = account.getTransactionHistory();
        writer.write("Transaction History (" + transactions.size() + " transactions):");
        writer.newLine();
        
        if (transactions.isEmpty()) {
            writer.write("  No transactions yet.");
            writer.newLine();
        } else {
            for (int i = 0; i < transactions.size(); i++) {
                writer.write("  " + (i + 1) + ". " + transactions.get(i));
                writer.newLine();
            }
        }
        
        writer.newLine();
    }
}