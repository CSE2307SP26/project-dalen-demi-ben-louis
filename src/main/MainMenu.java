package main;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 8;
    private static final int MAX_SELECTION = 8;

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
        System.out.println("8. Exit the app");
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
        }
    }

    private int selectAccount(String prompt) {
        System.out.println(prompt);
        for (int i = 0; i < accounts.size(); i++) {
            BankAccount acc = accounts.get(i);
            String type = acc.getAccountType();
            String balanceStr = " (Balance: $" + String.format("%.2f", acc.getBalance()) + ")";
            if (acc instanceof CheckingAccount && acc.getBalance() < 0) {
                balanceStr = " (Balance: $" + String.format("%.2f", acc.getBalance()) + " [OVERDRAWN])";
            }
            String status = acc.isClosed() ? " [CLOSED]" : balanceStr;
            System.out.println((i + 1) + ". " + type + " Account " + (i + 1) + status);
        }
        int selection = -1;
        while (selection < 1 || selection > accounts.size()) {
            System.out.print("Enter account number: ");
            selection = keyboardInput.nextInt();
        }
        return selection - 1;
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
        double depositAmount = getPositiveAmount("How much would you like to deposit: ");
        accounts.get(idx).deposit(depositAmount);
        System.out.println("Deposit successful!");
    }

    public void performWithdraw() {
        int idx = selectOpenAccount("Select account to withdraw from:");
        if (idx == -1) return;
        BankAccount account = accounts.get(idx);
        System.out.println("Current balance: $" + String.format("%.2f", account.getBalance()));
        if (account instanceof CheckingAccount) {
            CheckingAccount checking = (CheckingAccount) account;
            double availableFunds = account.getBalance() + checking.getOverdraftLimit();
            System.out.println("Overdraft limit: $" + String.format("%.2f", checking.getOverdraftLimit())
                + " | Available to withdraw: $" + String.format("%.2f", availableFunds));
        }
        double withdrawAmount = -1;
        while (withdrawAmount <= 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextDouble();
        }
        double withdrawAmount = getPositiveAmount("How much would you like to withdraw: ");
        try {
            double balanceBefore = account.getBalance();
            account.withdraw(withdrawAmount);
            System.out.println("Withdrawal successful. New balance: $" + String.format("%.2f", account.getBalance()));
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
        }
    }

    public void performCheckBalance() {
        int idx = selectAccount("Select account to check balance:");
        BankAccount account = accounts.get(idx);
        if (account.isClosed()) {
            System.out.println(account.getAccountType() + " Account " + (idx + 1) + " is closed.");
        } else {
            System.out.println("Balance for " + account.getAccountType() + " Account " + (idx + 1) + ": $" + String.format("%.2f", account.getBalance()));
            if (account instanceof CheckingAccount && account.getBalance() < 0) {
                System.out.println("WARNING: This account is overdrawn.");
            }
        }
    }

    public void displayTransactionHistory() {
        int idx = selectAccount("Select account to view history:");
        BankAccount account = accounts.get(idx);
        List<String> transactions = account.getTransactionHistory();
        System.out.println("\n=== Transaction History (" + account.getAccountType() + " Account " + (idx + 1) + ") ===");
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
        }
    }

    public void run() {
        System.out.println("Welcome to the 237 Bank App!");
        if (accounts.isEmpty()) {
            System.out.println("Let's start by opening your first account.");
            createNewAccount();
        }
        int selection = -1;
        while (selection != EXIT_SELECTION) {
            displayOptions();
            selection = getUserSelection(MAX_SELECTION);
            if (selection != EXIT_SELECTION) {
                processInput(selection);
            }
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}
