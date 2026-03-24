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
        this.accounts.add(new BankAccount());
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("What would you like to do?");
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
            String status = acc.isClosed() ? " [CLOSED]" : " (Balance: $" + String.format("%.2f", acc.getBalance()) + ")";
            System.out.println((i + 1) + ". Account " + (i + 1) + status);
        }
        int selection = -1;
        while (selection < 1 || selection > accounts.size()) {
            System.out.print("Enter account number: ");
            selection = keyboardInput.nextInt();
        }
        return selection - 1;
    }

    public void selectAccountAndDeposit() {
        int idx = selectAccount("Select account to deposit into:");
        if (accounts.get(idx).isClosed()) {
            System.out.println("Cannot deposit into a closed account.");
            return;
        }
        double depositAmount = -1;
        while (depositAmount <= 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextDouble();
        }
        accounts.get(idx).deposit(depositAmount);
        System.out.println("Deposit successful!");
    }

    public void performWithdraw() {
        int idx = selectAccount("Select account to withdraw from:");
        BankAccount account = accounts.get(idx);
        if (account.isClosed()) {
            System.out.println("Cannot withdraw from a closed account.");
            return;
        }
        System.out.println("Current balance: $" + String.format("%.2f", account.getBalance()));
        double withdrawAmount = -1;
        while (withdrawAmount <= 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextDouble();
        }
        try {
            account.withdraw(withdrawAmount);
            System.out.println("Withdrawal successful. New balance: $" + String.format("%.2f", account.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Insufficient funds. Your balance is: $" + String.format("%.2f", account.getBalance()));
        }
    }

    public void performCheckBalance() {
        int idx = selectAccount("Select account to check balance:");
        BankAccount account = accounts.get(idx);
        if (account.isClosed()) {
            System.out.println("Account " + (idx + 1) + " is closed.");
        } else {
            System.out.println("Balance for Account " + (idx + 1) + ": $" + String.format("%.2f", account.getBalance()));
        }
    }

    public void displayTransactionHistory() {
        int idx = selectAccount("Select account to view history:");
        BankAccount account = accounts.get(idx);
        List<String> transactions = account.getTransactionHistory();
        System.out.println("\n=== Transaction History (Account " + (idx + 1) + ") ===");
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
        accounts.add(new BankAccount());
        System.out.println("New account created! You now have " + accounts.size() + " accounts.");
    }

    public void closeAccount() {
        int idx = selectAccount("Select account to close:");
        BankAccount account = accounts.get(idx);
        if (account.isClosed()) {
            System.out.println("Account " + (idx + 1) + " is already closed.");
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
        int fromIdx = selectAccount("Select account to transfer FROM:");
        BankAccount fromAccount = accounts.get(fromIdx);
        if (fromAccount.isClosed()) {
            System.out.println("Cannot transfer from a closed account.");
            return;
        }
        int toIdx = selectAccount("Select account to transfer TO:");
        if (toIdx == fromIdx) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }
        BankAccount toAccount = accounts.get(toIdx);
        if (toAccount.isClosed()) {
            System.out.println("Cannot transfer to a closed account.");
            return;
        }
        System.out.println("Current balance: $" + String.format("%.2f", fromAccount.getBalance()));
        double amount = -1;
        while (amount <= 0) {
            System.out.print("How much would you like to transfer: ");
            amount = keyboardInput.nextDouble();
        }
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
