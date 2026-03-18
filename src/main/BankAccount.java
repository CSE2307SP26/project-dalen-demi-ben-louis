package main;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private double balance;
    private List<String> transactions;

    public BankAccount() {
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    public void deposit(double amount) {
        if(amount > 0) {
            this.balance += amount;
            this.transactions.add("Deposit: +$" + String.format("%.2f", amount));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException();
        }
        this.balance -= amount;
    }

    public double getBalance() {
        return this.balance;
    }

    // Return a copy of the transaction history to maintain encapsulation
    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }
}