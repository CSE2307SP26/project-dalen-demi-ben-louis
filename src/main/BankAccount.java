package main;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    protected double balance;
    protected List<String> transactions;
    private boolean isClosed;
    private double fees;
    private String pin;

    public BankAccount() {
        this.balance = 0;
        this.transactions = new ArrayList<>();
        this.isClosed = false;
        this.fees = 0;
        this.pin = null;
    }

    public void deposit(double amount) {
        if (isClosed) {
            throw new IllegalStateException("Account is closed.");
        }
        if(amount > 0) {
            this.balance += amount;
            this.transactions.add("Deposit: +$" + String.format("%.2f", amount));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void withdraw(double amount) {
        if (isClosed) {
            throw new IllegalStateException("Account is closed.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException();
        }
        this.balance -= amount;
        this.transactions.add("Withdrawal: -$" + String.format("%.2f", amount));
    }

    public void takeLoan(double amount) {
        if (isClosed) {
            throw new IllegalStateException("Account is closed.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Loan amount must be greater than zero.");
        }
        if (amount >= this.balance) {
            throw new IllegalArgumentException("Loan amount must be less than current account balance.");
        }
        this.balance += amount;
        this.transactions.add("Loan: +$" + String.format("%.2f", amount));
    }

    public void close() {
        if (isClosed) {
            throw new IllegalStateException("Account is already closed.");
        }
        isClosed = true;
    }

    public void transfer(BankAccount target, double amount) {
        if (isClosed) {
            throw new IllegalStateException("Source account is closed.");
        }
        if (target.isClosed()) {
            throw new IllegalStateException("Target account is closed.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException();
        }
        this.balance -= amount;
        this.transactions.add("Transfer Out: -$" + String.format("%.2f", amount));
        target.balance += amount;
        target.transactions.add("Transfer In: +$" + String.format("%.2f", amount));
    }

    public void collectFees(){
        if (isClosed) {
            throw new IllegalStateException("Account is closed.");
        }
        if (this.fees > 0){
            this.transactions.add("Fee: -$" + String.format("%.2f", this.fees));
            this.balance -= this.fees;
            this.fees = 0;
        }
    }

    public void addFees(double amount){
        if (isClosed) {
            throw new IllegalStateException("Account is closed.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        this.fees += amount;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public String getAccountType() {
        return "Standard";
    }

    public void setPin(String pin) {
        validatePin(pin);
        this.pin = pin;
    }

    public void clearPin() {
        this.pin = null;
    }

    public boolean hasPin() {
        return this.pin != null;
    }

    public boolean authenticate(String enteredPin) {
        if (!hasPin()) {
            return true;
        }
        return this.pin.equals(enteredPin);
    }

    public int getTransactionCount() {
        return transactions.size();
    }

    public double getTotalDeposits() {
        return sumTransactionsByPrefix("Deposit:");
    }

    public double getTotalWithdrawals() {
        return sumTransactionsByPrefix("Withdrawal:");
    }

    private double sumTransactionsByPrefix(String prefix) {
        double total = 0;
        for (String transaction : transactions) {
            if (transaction.startsWith(prefix)) {
                total += parseAmountFromTransaction(transaction);
            }
        }
        return total;
    }

    private double parseAmountFromTransaction(String transaction) {
        String amountStr = transaction.replaceAll("[^0-9.]", "");
        return Double.parseDouble(amountStr);
    }

    public double getBalance() {
        return this.balance;
    }

    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }

    private void validatePin(String pin) {
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }
    }
}
