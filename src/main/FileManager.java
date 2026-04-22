package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    
    public static void saveAccountData(List<BankAccount> accounts, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (BankAccount account : accounts) {
                writer.write("ACCOUNT");
                writer.newLine();
                writer.write("TYPE:" + account.getAccountType());
                writer.newLine();
                writer.write("BALANCE:" + String.format("%.2f", account.getBalance()));
                writer.newLine();
                writer.write("STATUS:" + (account.isClosed() ? "CLOSED" : "OPEN"));
                writer.newLine();
                if (account.hasNickname()) {
                    writer.write("NICKNAME:" + account.getNickname());
                    writer.newLine();
                }
                for (String transaction : account.getTransactionHistory()) {
                    writer.write("TRANSACTION:" + transaction);
                    writer.newLine();
                }
                writer.write("END_ACCOUNT");
                writer.newLine();
            }
        }
    }

    public static List<BankAccount> loadAccountData(String filename) throws IOException {
        List<BankAccount> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("ACCOUNT")) {
                    BankAccount account = parseAccount(reader);
                    accounts.add(account);
                }
            }
        }
        return accounts;
    }

    private static BankAccount parseAccount(BufferedReader reader) throws IOException {
        String type = "Standard";
        double balance = 0;
        boolean closed = false;
        String nickname = null;
        List<String> transactions = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("END_ACCOUNT")) {
                break;
            }
            if (line.startsWith("TYPE:")) {
                type = line.substring(5);
            } else if (line.startsWith("BALANCE:")) {
                balance = Double.parseDouble(line.substring(8));
            } else if (line.startsWith("STATUS:")) {
                closed = line.substring(7).equals("CLOSED");
            } else if (line.startsWith("NICKNAME:")) {
                nickname = line.substring(9);
            } else if (line.startsWith("TRANSACTION:")) {
                transactions.add(line.substring(12));
            }
        }

        BankAccount account;
        switch (type) {
            case "Checking":
                account = new CheckingAccount();
                break;
            case "Savings":
                account = new SavingsAccount();
                break;
            default:
                account = new BankAccount();
                break;
        }

        account.balance = balance;
        account.transactions.addAll(transactions);
        if (nickname != null) {
            account.setNickname(nickname);
        }
        if (closed) {
            account.close();
        }

        return account;
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