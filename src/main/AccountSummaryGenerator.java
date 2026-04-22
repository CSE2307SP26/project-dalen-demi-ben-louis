package main;

import java.util.List;

public class AccountSummaryGenerator {

    private List<BankAccount> accounts;

    public AccountSummaryGenerator(List<BankAccount> accounts) {
        this.accounts = accounts;
    }

    public double getTotalBalance() {
        double total = 0;
        for (BankAccount account : accounts) {
            if (!account.isClosed()) {
                total += account.getBalance();
            }
        }
        return total;
    }

    public int getTotalAccountCount() {
        return accounts.size();
    }

    public int getOpenAccountCount() {
        int count = 0;
        for (BankAccount account : accounts) {
            if (!account.isClosed()) {
                count++;
            }
        }
        return count;
    }

    public int getClosedAccountCount() {
        int count = 0;
        for (BankAccount account : accounts) {
            if (account.isClosed()) {
                count++;
            }
        }
        return count;
    }

    public double getTotalDeposits() {
        double total = 0;
        for (BankAccount account : accounts) {
            total += account.getTotalDeposits();
        }
        return total;
    }

    public double getTotalWithdrawals() {
        double total = 0;
        for (BankAccount account : accounts) {
            total += account.getTotalWithdrawals();
        }
        return total;
    }

}
