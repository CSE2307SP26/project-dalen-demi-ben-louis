package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_WITH_SAVE = 13;
    private static final int EXIT_WITHOUT_SAVE = 14;
    private static final int MAX_SELECTION = 14;

    private ArrayList<BankAccount> accounts;
    private InputHelper inputHelper;
    private AccountHandler accountHandler;
    private AccountSettingsHandler settingsHandler;

    public MainMenu() {
        this.accounts = new ArrayList<>();
        Scanner keyboardInput = new Scanner(System.in);
        this.inputHelper = new InputHelper(accounts, keyboardInput);
        this.accountHandler = new AccountHandler(accounts, inputHelper);
        this.settingsHandler = new AccountSettingsHandler(accounts, inputHelper);
    }

    public void displayOptions() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Make a deposit");
        System.out.println("2. Make a withdrawal");
        System.out.println("3. Check balance");
        System.out.println("4. View transaction history");
        System.out.println("5. Search transaction history");  
        System.out.println("6. Create a new account");
        System.out.println("7. Close an account");
        System.out.println("8. Transfer money between accounts");
        System.out.println("9. Manage account PIN");
        System.out.println("10. Set account nickname");
        System.out.println("11. Take out a loan");
        System.out.println("12. View account summary");
        System.out.println("13. Save and Exit");
        System.out.println("14. Exit without saving");
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1: accountHandler.deposit(); break;
            case 2: accountHandler.withdraw(); break;
            case 3: accountHandler.checkBalance(); break;
            case 4: accountHandler.displayTransactionHistory(); break;
            case 5: accountHandler.searchTransactionHistory(); break;
            case 6: accountHandler.createNewAccount(); break;
            case 7: accountHandler.closeAccount(); break;
            case 8: accountHandler.transferMoney(); break;
            case 9: settingsHandler.manageAccountPin(); break;
            case 10: settingsHandler.setAccountNickname(); break;
            case 11: settingsHandler.performLoan(); break;
            case 12: settingsHandler.displayAccountSummary(); break;
            case 13: saveAndExit(); break;
            case 14: // Exit without saving - handled in run() method
                break;
        }
    }  // <-- Fixed: Added missing closing brace for processInput method

    private void saveAndExit() {
        System.out.println("\n--- Save Accounts ---");
        System.out.println("1. Text format (.txt)");
        System.out.println("2. CSV format (.csv)");
        int choice = inputHelper.getUserSelection(2);
        saveToFormat(choice);
        System.out.println("\nThank you for using the 237 Bank App!");
        System.exit(0);
    }

    private void saveToFormat(int choice) {
        String extension = (choice == 1) ? ".txt" : ".csv";
        String filename = "bank_accounts_" + System.currentTimeMillis() + extension;
        try {
            if (choice == 1) {
                FileManager.saveAccountsToFile(accounts, filename);
            } else {
                FileManager.saveAccountsToCSV(accounts, filename);
            fromAccount.transfer(toAccount, amount);
            System.out.println("Transfer successful!");
            System.out.println("Account " + (fromIdx + 1) + " balance: $" + String.format("%.2f", fromAccount.getBalance()));
            System.out.println("Account " + (toIdx + 1) + " balance: $" + String.format("%.2f", toAccount.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Insufficient funds. Your balance is: $" + String.format("%.2f", fromAccount.getBalance()));
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchTransactionHistory() {
        int idx = inputHelper.selectAccount("Select account to search transactions:");
        BankAccount account = accounts.get(idx);
        if (!inputHelper.authenticateAccount(account)) return;
        
        System.out.print("Enter search keyword (e.g., 'Deposit', 'Withdrawal', 'Loan', 'Transfer', etc.): ");
        String keyword = inputHelper.readNextWord();
        
        List<String> searchResults = account.searchTransactions(keyword);
        
        System.out.println("\n=== Search Results for '" + keyword + "' in " + 
                           account.getDisplayName(idx + 1) + " ===");
        
        if (searchResults.isEmpty()) {
            System.out.println("No transactions found matching '" + keyword + "'.");
        } else {
            System.out.println("Found " + searchResults.size() + " transaction(s):\n");
            for (int i = 0; i < searchResults.size(); i++) {
                System.out.println((i + 1) + ". " + searchResults.get(i));
            }
            System.out.println("\nCurrent balance: $" + String.format("%.2f", account.getBalance()));
        }
        System.out.println("========================================\n");
    }

    private boolean displayWithdrawalInfo(BankAccount account) {
        if (account instanceof SavingsAccount) {
            SavingsAccount savings = (SavingsAccount) account;
            int remaining = savings.getRemainingWithdrawals();
            if (remaining <= 0) {
                System.out.println("ERROR: Monthly withdrawal limit reached.");
                return false;
            }
            System.out.println("Accounts saved successfully to: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    public void run() {
        System.out.println("Welcome to the 237 Bank App!");
        if (accounts.isEmpty()) {
            System.out.println("Let's start by opening your first account.");
            accountHandler.createNewAccount();
        }
        int selection = -1;
        while (selection != EXIT_WITHOUT_SAVE) {
            displayOptions();
            selection = inputHelper.getUserSelection(MAX_SELECTION);
            if (selection != EXIT_WITHOUT_SAVE) {
                processInput(selection);
            }
        }
        System.out.println("\nThank you for using the 237 Bank App!");
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}