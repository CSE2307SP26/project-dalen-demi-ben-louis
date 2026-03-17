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

    public double getBalance() {
        return this.balance;
    }
    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactions);  
    }
}
