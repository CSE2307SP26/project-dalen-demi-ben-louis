package main;

import java.util.ArrayList;

public class AccountSettingsHandler {

    private ArrayList<BankAccount> accounts;
    private InputHelper inputHelper;

    public AccountSettingsHandler(ArrayList<BankAccount> accounts, InputHelper inputHelper) {
        this.accounts = accounts;
        this.inputHelper = inputHelper;
    }

    public void manageAccountPin() {
        int idx = inputHelper.selectOpenAccount("Select account to manage PIN:");
        if (idx == -1) return;
        BankAccount account = accounts.get(idx);
        if (account.hasPin() && !inputHelper.authenticateAccount(account)) return;
        if (account.hasPin()) {
            modifyExistingPin(account);
        } else {
            createNewPin(account);
        }
    }

    public void setAccountNickname() {
        int idx = inputHelper.selectOpenAccount("Select account to nickname:");
        if (idx == -1) return;
        BankAccount account = accounts.get(idx);
        if (!inputHelper.authenticateAccount(account)) return;
        System.out.print("Enter a nickname for this account: ");
        String nickname = inputHelper.readNextWord();
        account.setNickname(nickname);
        System.out.println("Nickname set to \"" + nickname + "\".");
    }

    public void performLoan() {
        int idx = inputHelper.selectOpenAccount("Select account to take a loan from:");
        if (idx == -1) return;
        BankAccount account = accounts.get(idx);
        if (!inputHelper.authenticateAccount(account)) return;
        System.out.println("Current balance: $" + String.format("%.2f", account.getBalance()));
        if (account.getBalance() <= 0) {
            System.out.println("This account must have a positive balance before taking a loan.");
            return;
        }
        double amount = inputHelper.getPositiveAmount("How much would you like to borrow: ");
        try {
            account.takeLoan(amount);
            System.out.println("Loan successful! New balance: $" + String.format("%.2f", account.getBalance()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayAccountSummary() {
        int idx = inputHelper.selectAccount("Select account to view summary:");
        BankAccount account = accounts.get(idx);
        if (!inputHelper.authenticateAccount(account)) return;
        printSummaryHeader(account, idx + 1);
        printSummaryDetails(account);
    }

    private void modifyExistingPin(BankAccount account) {
        System.out.println("\n1. Change PIN");
        System.out.println("2. Remove PIN");
        int choice = inputHelper.getUserSelection(2);
        if (choice == 1) {
            String newPin = inputHelper.getFourDigitPin("Enter new 4-digit PIN: ");
            account.setPin(newPin);
            System.out.println("PIN updated successfully.");
        } else {
            account.clearPin();
            System.out.println("PIN removed successfully.");
        }
    }

    private void createNewPin(BankAccount account) {
        String pin = inputHelper.getFourDigitPin("Set a new 4-digit PIN: ");
        account.setPin(pin);
        System.out.println("PIN set successfully.");
    }

    private void printSummaryHeader(BankAccount account, int accountNumber) {
        String status = account.isClosed() ? "Closed" : "Open";
        System.out.println("\n=== Account Summary ===");
        System.out.println("Account:      " + account.getAccountType() + " Account " + accountNumber);
        System.out.println("Status:       " + status);
    }

    private void printSummaryDetails(BankAccount account) {
        System.out.println("Balance:      $" + String.format("%.2f", account.getBalance()));
        System.out.println("Deposits:     $" + String.format("%.2f", account.getTotalDeposits()));
        System.out.println("Withdrawals:  $" + String.format("%.2f", account.getTotalWithdrawals()));
        System.out.println("Transactions: " + account.getTransactionCount());
        System.out.println("=======================\n");
    }
}
