package main;

import java.util.ArrayList;
import java.util.Scanner;

public class InputHelper {

    private ArrayList<BankAccount> accounts;
    private Scanner keyboardInput;

    public InputHelper(ArrayList<BankAccount> accounts, Scanner keyboardInput) {
        this.accounts = accounts;
        this.keyboardInput = keyboardInput;
    }

    public int selectAccount(String prompt) {
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

    public int selectOpenAccount(String prompt) {
        int idx = selectAccount(prompt);
        if (accounts.get(idx).isClosed()) {
            System.out.println("Cannot perform operation on a closed account.");
            return -1;
        }
        return idx;
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
        }
        return selection;
    }

    public double getPositiveAmount(String prompt) {
        double amount = -1;
        while (amount <= 0) {
            System.out.print(prompt);
            amount = keyboardInput.nextDouble();
        }
        return amount;
    }

    public boolean authenticateAccount(BankAccount account) {
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

    public String getFourDigitPin(String prompt) {
        while (true) {
            System.out.print(prompt);
            String pin = keyboardInput.next();
            if (pin.matches("\\d{4}")) {
                return pin;
            }
            System.out.println("Invalid PIN. PIN must be exactly 4 digits.");
        }
    }

    public String readNextWord() {
        return keyboardInput.next();
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
}
