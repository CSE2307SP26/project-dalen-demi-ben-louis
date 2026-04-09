package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_WITH_SAVE = 12;
    private static final int EXIT_WITHOUT_SAVE = 13;
    private static final int MAX_SELECTION = 13;

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
        System.out.println("5. Create a new account");
        System.out.println("6. Close an account");
        System.out.println("7. Transfer money between accounts");
        System.out.println("8. Manage account PIN");
        System.out.println("9. Set account nickname");
        System.out.println("10. Take out a loan");
        System.out.println("11. View account summary");
        System.out.println("12. Save and Exit");
        System.out.println("13. Exit without saving");
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1: accountHandler.deposit(); break;
            case 2: accountHandler.withdraw(); break;
            case 3: accountHandler.checkBalance(); break;
            case 4: accountHandler.displayTransactionHistory(); break;
            case 5: accountHandler.createNewAccount(); break;
            case 6: accountHandler.closeAccount(); break;
            case 7: accountHandler.transferMoney(); break;
            case 8: settingsHandler.manageAccountPin(); break;
            case 9: settingsHandler.setAccountNickname(); break;
            case 10: settingsHandler.performLoan(); break;
            case 11: settingsHandler.displayAccountSummary(); break;
            case 12: saveAndExit(); break;
        }
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
