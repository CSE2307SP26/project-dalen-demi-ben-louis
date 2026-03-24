package main;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private double balance;
    private List<String> transactions;
    private boolean isClosed;
    private double fees;

    public BankAccount() {
        this.balance = 0;
        this.transactions = new ArrayList<>();
        this.isClosed = false;
        this.fees = 0;
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
        this.transactions.add("Fee: -$" + String.format("%.2f", this.fees));
        this.balance -= this.fees;
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

    public double getBalance() {
        return this.balance;
    }

    // Return a copy of the transaction history to maintain encapsulation
    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }
}