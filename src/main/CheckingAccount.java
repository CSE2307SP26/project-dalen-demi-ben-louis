package main;

public class CheckingAccount extends BankAccount {

    private static final double OVERDRAFT_LIMIT = 100.0;
    private static final double OVERDRAFT_FEE = 35.0;

    @Override
    public String getAccountType() {
        return "Checking";
    }

    @Override
    public void withdraw(double amount) {
        if (isClosed()) {
            throw new IllegalStateException("Account is closed.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        if (amount > this.balance + OVERDRAFT_LIMIT) {
            throw new IllegalArgumentException();
        }
        this.balance -= amount;
        this.transactions.add("Withdrawal: -$" + String.format("%.2f", amount));
        if (this.balance < 0) {
            this.balance -= OVERDRAFT_FEE;
            this.transactions.add("Overdraft Fee: -$" + String.format("%.2f", OVERDRAFT_FEE));
        }
    }

    public double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }

}
