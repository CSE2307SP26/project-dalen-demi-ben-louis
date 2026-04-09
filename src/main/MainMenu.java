package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_WITH_SAVE = 11;
    private static final int EXIT_WITHOUT_SAVE = 12;
    private static final int MAX_SELECTION = 12;

    private ArrayList<BankAccount> accounts;
    private Scanner keyboardInput;

    public MainMenu() {
        this.accounts = new ArrayList<>();
        this.keyboardInput = new Scanner(System.in);
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
        System.out.println("11. Save and Exit");
        System.out.println("12. Exit without saving");
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
        }
        return selection;
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1:
                selectAccountAndDeposit();
                break;
            case 2:
                performWithdraw();
                break;
            case 3:
                performCheckBalance();
                break;
            case 4:
                displayTransactionHistory();
                break;
            case 5:
                createNewAccount();
                break;
            case 6:
                closeAccount();
                break;
            case 7:
                transferMoney();
                break;
            case 8:
                manageAccountPin();
                break;
            case 9:
                setAccountNickname();
                break;
            case 10:
                 performLoan();
                 break;
            case 11:
                saveAndExit();
                break;
        }
    }

    private int selectAccount(String prompt) {
        System.out.println(prompt);
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + formatAccountLabel(accounts.get(i), i + 1));
        }
        int selection = -1;
        while (selection < 1 || selection > accounts.size()) {
            System.out.print("Enter account number: ");
            selection = keyboardInput.nextInt();
        }
        return selection - 1;
    }

    private String formatAccountLabel(BankAccount account, int accountNumber) {
        String name = account.getDisplayName(accountNumber);
        String status = account.isClosed() ? " [CLOSED]" : formatBalanceLabel(account);
        String pinLabel = account.hasPin() ? " [PIN PROTECTED]" : "";
        return name + status + pinLabel;
    }

    private String formatBalanceLabel(BankAccount account) {
        String balanceStr = String.format("%.2f", account.getBalance());
        if (account instanceof CheckingAccount && account.getBalance() < 0) {
            return " (Balance: $" + balanceStr + " [OVERDRAWN])";
        }
        return " (Balance: $" + balanceStr + ")";
    }

    private int selectOpenAccount(String prompt) {
        int idx = selectAccount(prompt);
        if (accounts.get(idx).isClosed()) {
            System.out.println("Cannot perform operation on a closed account.");
            return -1;
        }
        return idx;
    }

    private double getPositiveAmount(String prompt) {
        double amount = -1;
        while (amount <= 0) {
            System.out.print(prompt);
            amount = keyboardInput.nextDouble();
        }
        return amount;
    }

    public void selectAccountAndDeposit() {
        int idx = selectOpenAccount("Select account to deposit into:");
        if (idx == -1) return;
        if (!authenticateAccount(accounts.get(idx))) return;
        double depositAmount = getPositiveAmount("How much would you like to deposit: ");
        accounts.get(idx).deposit(depositAmount);
        System.out.println("Deposit successful!");
    }

    public void performWithdraw() {
        int idx = selectOpenAccount("Select account to withdraw from:");
        if (idx == -1) return;
        BankAccount account = accounts.get(idx);
        if (!authenticateAccount(account)) return;
        System.out.println("Current balance: $" + String.format("%.2f", account.getBalance()));
        
        // Add check for Savings account withdrawal limit
        if (account instanceof SavingsAccount) {
            SavingsAccount savings = (SavingsAccount) account;
            int remaining = savings.getRemainingWithdrawals();
            if (remaining <= 0) {
                System.out.println("ERROR: You have reached the maximum of 6 withdrawals for this month.");
                System.out.println("Please wait until next month to make more withdrawals.");
                return;
            }
            System.out.println("Remaining withdrawals this month: " + remaining);
        }
        
        if (account instanceof CheckingAccount) {
            CheckingAccount checking = (CheckingAccount) account;
            double availableFunds = account.getBalance() + checking.getOverdraftLimit();
            System.out.println("Overdraft limit: $" + String.format("%.2f", checking.getOverdraftLimit())
                + " | Available to withdraw: $" + String.format("%.2f", availableFunds));
        }
        
        double withdrawAmount = getPositiveAmount("How much would you like to withdraw: ");
        
        try {
            double balanceBefore = account.getBalance();
            account.withdraw(withdrawAmount);
            System.out.println("Withdrawal successful. New balance: $" + String.format("%.2f", account.getBalance()));
            
            if (account instanceof SavingsAccount) {
                SavingsAccount savings = (SavingsAccount) account;
                System.out.println("Withdrawals used this month: " + savings.getWithdrawalCount());
            }
            
            if (account instanceof CheckingAccount && account.getBalance() < 0 && balanceBefore >= 0) {
                System.out.println("WARNING: Account is now overdrawn. A $35.00 overdraft fee has been applied.");
            } else if (account instanceof CheckingAccount && account.getBalance() < 0 && balanceBefore < 0) {
                System.out.println("WARNING: Account remains overdrawn. A $35.00 overdraft fee has been applied.");
            }
        } catch (IllegalArgumentException e) {
            if (account instanceof CheckingAccount) {
                System.out.println("Exceeds overdraft limit. Your balance is: $" + String.format("%.2f", account.getBalance()));
            } else {
                System.out.println("Insufficient funds. Your balance is: $" + String.format("%.2f", account.getBalance()));
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void performCheckBalance() {
        int idx = selectAccount("Select account to check balance:");
        BankAccount account = accounts.get(idx);
        if (!authenticateAccount(account)) return;
        String displayName = account.getDisplayName(idx + 1);
        if (account.isClosed()) {
            System.out.println(displayName + " is closed.");
        } else {
            System.out.println("Balance for " + displayName + ": $" + String.format("%.2f", account.getBalance()));
            if (account instanceof CheckingAccount && account.getBalance() < 0) {
                System.out.println("WARNING: This account is overdrawn.");
            }
        }
    }

    public void displayTransactionHistory() {
        int idx = selectAccount("Select account to view history:");
        BankAccount account = accounts.get(idx);
        if (!authenticateAccount(account)) return;
        List<String> transactions = account.getTransactionHistory();
        System.out.println("\n=== Transaction History (" + account.getDisplayName(idx + 1) + ") ===");
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (String transaction : transactions) {
                System.out.println(transaction);
            }
            System.out.println("Current balance: $" + String.format("%.2f", account.getBalance()));
        }
        System.out.println("==========================\n");
    }

    public void createNewAccount() {
        System.out.println("What type of account would you like to open?");
        System.out.println("1. Checking");
        System.out.println("2. Savings");
        int type = -1;
        while (type < 1 || type > 2) {
            System.out.print("Please make a selection: ");
            type = keyboardInput.nextInt();
        }
        if (type == 1) {
            accounts.add(new CheckingAccount());
            System.out.println("New Checking account created! You now have " + accounts.size() + " account(s).");
        } else {
            accounts.add(new SavingsAccount());
            System.out.println("New Savings account created! You now have " + accounts.size() + " account(s).");
        }
    }

    public void closeAccount() {
        int idx = selectAccount("Select account to close:");
        BankAccount account = accounts.get(idx);
        if (!authenticateAccount(account)) return;
        if (account.isClosed()) {
            System.out.println("Account " + (idx + 1) + " is already closed.");
            return;
        }
        if (account.getBalance() < 0) {
            System.out.println("Account " + (idx + 1) + " has an outstanding overdraft balance of $"
                + String.format("%.2f", Math.abs(account.getBalance())) + ". Please deposit to cover the balance before closing.");
            return;
        }
        if (Math.round(account.getBalance() * 100) > 0) {
            System.out.println("Remaining balance of $" + String.format("%.2f", account.getBalance()) + " has been withdrawn.");
            account.withdraw(account.getBalance());
        }
        account.close();
        System.out.println("Account " + (idx + 1) + " has been closed.");
    }

    public void transferMoney() {
        if (accounts.size() < 2) {
            System.out.println("You need at least two accounts to transfer money.");
            return;
        }
        int fromIdx = selectOpenAccount("Select account to transfer FROM:");
        if (fromIdx == -1) return;
        if (!authenticateAccount(accounts.get(fromIdx))) return;
        int toIdx = selectOpenAccount("Select account to transfer TO:");
        if (toIdx == -1) return;
        if (toIdx == fromIdx) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }
        BankAccount fromAccount = accounts.get(fromIdx);
        BankAccount toAccount = accounts.get(toIdx);
        System.out.println("Current balance: $" + String.format("%.2f", fromAccount.getBalance()));
        double amount = getPositiveAmount("How much would you like to transfer: ");
        try {
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

    public void setAccountNickname() {
        int idx = selectOpenAccount("Select account to nickname:");
        if (idx == -1) return;
        BankAccount account = accounts.get(idx);
        if (!authenticateAccount(account)) return;
        System.out.print("Enter a nickname for this account: ");
        String nickname = keyboardInput.next();
        account.setNickname(nickname);
        System.out.println("Nickname set to \"" + nickname + "\".");
    }
      
    public void performLoan() {
        int idx = selectOpenAccount("Select account to take a loan from:");
        if (idx == -1) return;

        BankAccount account = accounts.get(idx);
        if (!authenticateAccount(account)) return;

        System.out.println("Current balance: $" + String.format("%.2f", account.getBalance()));
        if (account.getBalance() <= 0) {
            System.out.println("This account must have a positive balance before taking a loan.");
            return;
        }

        double amount = getPositiveAmount("How much would you like to borrow: ");
        try {
            account.takeLoan(amount);
            System.out.println("Loan successful! New balance: $" + String.format("%.2f", account.getBalance()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void manageAccountPin() {
        int idx = selectOpenAccount("Select account to manage PIN:");
        if (idx == -1) return;

        BankAccount account = accounts.get(idx);
        if (account.hasPin() && !authenticateAccount(account)) return;

        System.out.println("\nPIN options:");
        if (account.hasPin()) {
            System.out.println("1. Change PIN");
            System.out.println("2. Remove PIN");
            int choice = -1;
            while (choice < 1 || choice > 2) {
                System.out.print("Please make a selection: ");
                choice = keyboardInput.nextInt();
            }

            if (choice == 1) {
                String newPin = getFourDigitPin("Enter new 4-digit PIN: ");
                account.setPin(newPin);
                System.out.println("PIN updated successfully.");
            } else {
                account.clearPin();
                System.out.println("PIN removed successfully.");
            }
            return;
        }

        String pin = getFourDigitPin("Set a new 4-digit PIN: ");
        account.setPin(pin);
        System.out.println("PIN set successfully.");
    }

    private boolean authenticateAccount(BankAccount account) {
        if (!account.hasPin()) {
            return true;
        }

        final int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.print("Enter 4-digit PIN: ");
            String enteredPin = keyboardInput.next();
            if (account.authenticate(enteredPin)) {
                return true;
            }
            int attemptsRemaining = maxAttempts - attempt;
            if (attemptsRemaining > 0) {
                System.out.println("Incorrect PIN. Attempts remaining: " + attemptsRemaining);
            }
        }

        System.out.println("Authentication failed. Operation cancelled.");
        return false;
    }

    private String getFourDigitPin(String prompt) {
        while (true) {
            System.out.print(prompt);
            String pin = keyboardInput.next();
            if (pin.matches("\\d{4}")) {
                return pin;
            }
            System.out.println("Invalid PIN. PIN must be exactly 4 digits.");
        }
    }

    private void saveAndExit() {
        System.out.println("\n--- Save Accounts ---");
        System.out.println("Choose save format:");
        System.out.println("1. Text format (.txt)");
        System.out.println("2. CSV format (.csv)");
        System.out.print("Enter choice (1 or 2): ");
        
        int choice = -1;
        while (choice < 1 || choice > 2) {
            choice = keyboardInput.nextInt();
            if (choice < 1 || choice > 2) {
                System.out.print("Invalid choice. Please enter 1 or 2: ");
            }
        }
        
        String filename;
        if (choice == 1) {
            filename = "bank_accounts_" + System.currentTimeMillis() + ".txt";
            try {
                FileManager.saveAccountsToFile(accounts, filename);
                System.out.println("\n✓ Accounts saved successfully to: " + filename);
                System.out.println("  Location: " + new java.io.File(filename).getAbsolutePath());
            } catch (IOException e) {
                System.out.println("✗ Error saving accounts: " + e.getMessage());
            }
        } else {
            filename = "bank_accounts_" + System.currentTimeMillis() + ".csv";
            try {
                FileManager.saveAccountsToCSV(accounts, filename);
                System.out.println("\n✓ Accounts saved successfully to: " + filename);
                System.out.println("  Location: " + new java.io.File(filename).getAbsolutePath());
            } catch (IOException e) {
                System.out.println("✗ Error saving accounts: " + e.getMessage());
            }
        }
        
        System.out.println("\nThank you for using the 237 Bank App!");
        System.exit(0);
    }

    public void run() {
        System.out.println("Welcome to the 237 Bank App!");
        if (accounts.isEmpty()) {
            System.out.println("Let's start by opening your first account.");
            createNewAccount();
        }
        int selection = -1;
        while (selection != EXIT_WITHOUT_SAVE) {
            displayOptions();
            selection = getUserSelection(MAX_SELECTION);
            if (selection == EXIT_WITH_SAVE) {
                saveAndExit();
            } else if (selection != EXIT_WITHOUT_SAVE) {
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