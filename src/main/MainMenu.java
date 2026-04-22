package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static final int ADMIN_MENU = 16;
    private static final int EXIT_WITH_SAVE = 17;
    private static final int EXIT_WITHOUT_SAVE = 18;
    private static final int MAX_SELECTION = 18;

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
        System.out.println("5. View mini-statement (last N transactions)");
        System.out.println("6. Search transaction history");
        System.out.println("7. Create a new account");
        System.out.println("8. Close an account");
        System.out.println("9. Transfer money between accounts");
        System.out.println("10. Manage account PIN");
        System.out.println("11. Set account nickname");
        System.out.println("12. Take out a loan");
        System.out.println("13. View account summary");
        System.out.println("14. View combined summary (all accounts)");
        System.out.println("15. Manage daily withdrawal limit");
        System.out.println("16. Administrator menu");
        System.out.println("17. Save and Exit");
        System.out.println("18. Exit without saving");
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1: accountHandler.deposit(); break;
            case 2: accountHandler.withdraw(); break;
            case 3: accountHandler.checkBalance(); break;
            case 4: accountHandler.displayTransactionHistory(); break;
            case 5: accountHandler.displayMiniStatement(); break;
            case 6: accountHandler.searchTransactionHistory(); break;
            case 7: accountHandler.createNewAccount(); break;
            case 8: accountHandler.closeAccount(); break;
            case 9: accountHandler.transferMoney(); break;
            case 10: settingsHandler.manageAccountPin(); break;
            case 11: settingsHandler.setAccountNickname(); break;
            case 12: settingsHandler.performLoan(); break;
            case 13: settingsHandler.displayAccountSummary(); break;
            case 14: settingsHandler.displayCombinedSummary(); break;
            case 15: settingsHandler.manageDailyWithdrawalLimit(); break;
            case ADMIN_MENU: runAdminMenu(); break;
            case EXIT_WITH_SAVE: saveAndExit(); break;
            case EXIT_WITHOUT_SAVE: break;
        }
    }

    private void runAdminMenu() {
        int selection = -1;
        while (selection != 7) {
            displayAdminOptions();
            selection = inputHelper.getUserSelection(7);
            processAdminSelection(selection);
        }
    }

    private void displayAdminOptions() {
        System.out.println("\n--- Administrator Menu ---");
        System.out.println("1. Open account (any type)");
        System.out.println("2. Close account (override restrictions)");
        System.out.println("3. Add fee to account");
        System.out.println("4. Collect fees from account");
        System.out.println("5. Apply interest to one account");
        System.out.println("6. Apply interest to all open accounts");
        System.out.println("7. Back to main menu");
    }

    private void processAdminSelection(int selection) {
        switch (selection) {
            case 1:
                adminOpenAccount();
                break;
            case 2:
                adminCloseAccount();
                break;
            case 3:
                adminAddFeeToAccount();
                break;
            case 4:
                adminCollectFeesFromAccount();
                break;
            case 5:
                adminApplyInterestToSingleAccount();
                break;
            case 6:
                adminApplyInterestToAllOpenAccounts();
                break;
            default:
                break;
        }
    }

    private void adminOpenAccount() {
        System.out.println("Select account type to open:");
        System.out.println("1. Standard");
        System.out.println("2. Checking");
        System.out.println("3. Savings");

        int type = inputHelper.getUserSelection(3);
        if (type == 1) {
            accounts.add(new BankAccount());
            System.out.println("New Standard account created!");
        } else if (type == 2) {
            accounts.add(new CheckingAccount());
            System.out.println("New Checking account created!");
        } else {
            accounts.add(new SavingsAccount());
            System.out.println("New Savings account created!");
        }
    }

    private void adminCloseAccount() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        int idx = inputHelper.selectAccount("Select account to close (admin override):");
        BankAccount account = accounts.get(idx);
        if (account.isClosed()) {
            System.out.println("Account is already closed.");
            return;
        }

        account.close();
        System.out.println("Account closed by administrator.");
    }

    private void adminAddFeeToAccount() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        int idx = inputHelper.selectOpenAccount("Select account to add fee to:");
        if (idx == -1) {
            return;
        }

        double amount = inputHelper.getPositiveAmount("Fee amount: ");
        accounts.get(idx).addFees(amount);
        System.out.println("Fee added successfully.");
    }

    private void adminCollectFeesFromAccount() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        int idx = inputHelper.selectOpenAccount("Select account to collect fees from:");
        if (idx == -1) {
            return;
        }

        accounts.get(idx).collectFees();
        System.out.println("Fees collected.");
    }

    private void adminApplyInterestToSingleAccount() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        int idx = inputHelper.selectOpenAccount("Select account to apply interest to:");
        if (idx == -1) {
            return;
        }

        double rate = inputHelper.getPositiveAmount("Interest rate (%): ");
        try {
            accounts.get(idx).applyInterest(rate);
            System.out.println("Interest applied successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void adminApplyInterestToAllOpenAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        double rate = inputHelper.getPositiveAmount("Interest rate for all open accounts (%): ");
        int appliedCount = 0;
        for (BankAccount account : accounts) {
            if (!account.isClosed()) {
                try {
                    account.applyInterest(rate);
                    appliedCount++;
                } catch (IllegalStateException e) {
                    // Skip open accounts that are ineligible (e.g., non-positive balance).
                }
            }
        }

        System.out.println("Interest applied to " + appliedCount + " account(s).");
    }

    private void saveAndExit() {
        System.out.println("\n--- Save Accounts ---");
        System.out.println("1. Text report (.txt)");
        System.out.println("2. CSV report (.csv)");
        System.out.println("3. Loadable data file (.dat)");
        int choice = inputHelper.getUserSelection(3);
        saveToFormat(choice);
        System.out.println("\nThank you for using the 237 Bank App!");
        System.exit(0);
    }

    private void saveToFormat(int choice) {
        try {
            String filename;
            if (choice == 1) {
                filename = "bank_accounts_" + System.currentTimeMillis() + ".txt";
                FileManager.saveAccountsToFile(accounts, filename);
            } else if (choice == 2) {
                filename = "bank_accounts_" + System.currentTimeMillis() + ".csv";
                FileManager.saveAccountsToCSV(accounts, filename);
            } else {
                filename = "bank_accounts.dat";
                FileManager.saveAccountData(accounts, filename);
            }
            System.out.println("Accounts saved successfully to: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    private boolean offerLoadFromFile() {
        File dataFile = new File("bank_accounts.dat");
        if (!dataFile.exists()) {
            return false;
        }
        System.out.println("Saved account data found (bank_accounts.dat).");
        System.out.println("1. Load saved accounts");
        System.out.println("2. Start fresh");
        int choice = inputHelper.getUserSelection(2);
        if (choice == 1) {
            try {
                List<BankAccount> loaded = FileManager.loadAccountData("bank_accounts.dat");
                accounts.addAll(loaded);
                System.out.println("Loaded " + loaded.size() + " account(s) successfully!");
                return true;
            } catch (IOException e) {
                System.out.println("Error loading accounts: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    public void run() {
        System.out.println("Welcome to the 237 Bank App!");
        offerLoadFromFile();
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
