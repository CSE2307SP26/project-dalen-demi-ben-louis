package main;

import java.util.ArrayList;
import java.util.List;

public class AccountHandler {

    private ArrayList<BankAccount> accounts;
    private InputHelper inputHelper;

    public AccountHandler(ArrayList<BankAccount> accounts, InputHelper inputHelper) {
        this.accounts = accounts;
        this.inputHelper = inputHelper;
    }

    public void deposit() {
        int idx = inputHelper.selectOpenAccount("Select account to deposit into:");
        if (idx == -1) return;
        if (!inputHelper.authenticateAccount(accounts.get(idx))) return;
        double amount = inputHelper.getPositiveAmount("How much would you like to deposit: ");
        accounts.get(idx).deposit(amount);
        System.out.println("Deposit successful!");
    }

    public void withdraw() {
        int idx = inputHelper.selectOpenAccount("Select account to withdraw from:");
        if (idx == -1) return;
        BankAccount account = accounts.get(idx);
        if (!inputHelper.authenticateAccount(account)) return;
        System.out.println("Current balance: $" + String.format("%.2f", account.getBalance()));
        if (!displayWithdrawalInfo(account)) return;
        double amount = inputHelper.getPositiveAmount("How much would you like to withdraw: ");
        executeWithdrawal(account, amount);
    }

    public void checkBalance() {
        int idx = inputHelper.selectAccount("Select account to check balance:");
        BankAccount account = accounts.get(idx);
        if (!inputHelper.authenticateAccount(account)) return;
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
        int idx = inputHelper.selectAccount("Select account to view history:");
        BankAccount account = accounts.get(idx);
        if (!inputHelper.authenticateAccount(account)) return;
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
        int type = inputHelper.getUserSelection(2);
        if (type == 1) {
            accounts.add(new CheckingAccount());
            System.out.println("New Checking account created! You now have " + accounts.size() + " account(s).");
        } else {
            accounts.add(new SavingsAccount());
            System.out.println("New Savings account created! You now have " + accounts.size() + " account(s).");
        }
    }

    public void closeAccount() {
        int idx = inputHelper.selectAccount("Select account to close:");
        BankAccount account = accounts.get(idx);
        if (!inputHelper.authenticateAccount(account)) return;
        if (account.isClosed()) {
            System.out.println("Account " + (idx + 1) + " is already closed.");
            return;
        }
        if (account.getBalance() < 0) {
            System.out.println("Account has an outstanding overdraft balance. Please deposit to cover it before closing.");
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
        int fromIdx = inputHelper.selectOpenAccount("Select account to transfer FROM:");
        if (fromIdx == -1) return;
        if (!inputHelper.authenticateAccount(accounts.get(fromIdx))) return;
        int toIdx = inputHelper.selectOpenAccount("Select account to transfer TO:");
        if (toIdx == -1) return;
        if (toIdx == fromIdx) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }
        BankAccount fromAccount = accounts.get(fromIdx);
        BankAccount toAccount = accounts.get(toIdx);
        System.out.println("Current balance: $" + String.format("%.2f", fromAccount.getBalance()));
        double amount = inputHelper.getPositiveAmount("How much would you like to transfer: ");
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

    private boolean displayWithdrawalInfo(BankAccount account) {
        if (account instanceof SavingsAccount) {
            SavingsAccount savings = (SavingsAccount) account;
            int remaining = savings.getRemainingWithdrawals();
            if (remaining <= 0) {
                System.out.println("ERROR: Monthly withdrawal limit reached.");
                return false;
            }
            System.out.println("Remaining withdrawals this month: " + remaining);
        }
        if (account instanceof CheckingAccount) {
            CheckingAccount checking = (CheckingAccount) account;
            double availableFunds = account.getBalance() + checking.getOverdraftLimit();
            System.out.println("Available to withdraw: $" + String.format("%.2f", availableFunds));
        }
        return true;
    }

    private void executeWithdrawal(BankAccount account, double amount) {
        try {
            double balanceBefore = account.getBalance();
            account.withdraw(amount);
            System.out.println("Withdrawal successful. New balance: $" + String.format("%.2f", account.getBalance()));
            printPostWithdrawalInfo(account, balanceBefore);
        } catch (IllegalArgumentException e) {
            printWithdrawalError(account);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printPostWithdrawalInfo(BankAccount account, double balanceBefore) {
        if (account instanceof SavingsAccount) {
            System.out.println("Withdrawals used this month: " + ((SavingsAccount) account).getWithdrawalCount());
        }
        if (account instanceof CheckingAccount && account.getBalance() < 0) {
            System.out.println("WARNING: Account is overdrawn. A $35.00 overdraft fee has been applied.");
        }
    }

    private void printWithdrawalError(BankAccount account) {
        if (account instanceof CheckingAccount) {
            System.out.println("Exceeds overdraft limit. Your balance is: $" + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Insufficient funds. Your balance is: $" + String.format("%.2f", account.getBalance()));
        }
    }
}
