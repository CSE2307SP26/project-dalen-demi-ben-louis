package main;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private double balance;
    private List<String> transactions;
    private boolean isClosed;

    public BankAccount() {
        this.balance = 0;
        this.transactions = new ArrayList<>();
        this.isClosed = false;
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
    }

    public void close() {
        if (isClosed) {
            throw new IllegalStateException("Account is already closed.");
        }
        isClosed = true;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public double getBalance() {
        return this.balance;
    }

    // Return a copy of the transaction history to maintain encapsulation
    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }
}