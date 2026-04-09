package main;

public class SavingsAccount extends BankAccount {
    
    private static final int MAX_WITHDRAWALS_PER_MONTH = 6;
    private static final double INTEREST_RATE = 0.02; // 2% annual interest
    private int withdrawalCount;
    private int currentMonth;
    
    public SavingsAccount() {
        super();
        this.withdrawalCount = 0;
        this.currentMonth = getCurrentMonth();
    }
    
    // New method to calculate and apply interest
    public void applyInterest() {
        if (isClosed()) {
            throw new IllegalStateException("Account is closed.");
        }
        if (this.balance > 0) {
            double interest = this.balance * INTEREST_RATE;
            this.balance += interest;
            this.transactions.add("Interest: +$" + String.format("%.2f", interest) + 
                                 " (" + (INTEREST_RATE * 100) + "% interest)");
        }
    }
    
    // New method to get the current interest rate
    public double getInterestRate() {
        return INTEREST_RATE;
    }
    
    @Override
    public String getAccountType() {
        return "Savings";
    }
    
    @Override
    public void withdraw(double amount) {
        if (isClosed()) {
            throw new IllegalStateException("Account is closed.");
        }
        
        // Check if we need to reset the monthly counter
        int month = getCurrentMonth();
        if (month != currentMonth) {
            withdrawalCount = 0;
            currentMonth = month;
        }
        
        // Check withdrawal limit for Savings accounts
        if (withdrawalCount >= MAX_WITHDRAWALS_PER_MONTH) {
            throw new IllegalStateException("Monthly withdrawal limit exceeded. Maximum " + 
                                          MAX_WITHDRAWALS_PER_MONTH + " withdrawals per month.");
        }
        
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        
        this.balance -= amount;
        this.transactions.add("Withdrawal: -$" + String.format("%.2f", amount));
        withdrawalCount++;
    }
    
    public int getWithdrawalCount() {
        int month = getCurrentMonth();
        if (month != currentMonth) {
            return 0;
        }
        return withdrawalCount;
    }
    
    public int getRemainingWithdrawals() {
        int month = getCurrentMonth();
        if (month != currentMonth) {
            return MAX_WITHDRAWALS_PER_MONTH;
        }
        return MAX_WITHDRAWALS_PER_MONTH - withdrawalCount;
    }
    
    private int getCurrentMonth() {
        return java.time.LocalDate.now().getMonthValue();
    }
    
    @Override
    public void transfer(BankAccount target, double amount) {
        if (isClosed()) {
            throw new IllegalStateException("Source account is closed.");
        }
        if (target.isClosed()) {
            throw new IllegalStateException("Target account is closed.");
        }
        
        // Check if we need to reset the monthly counter
        int month = getCurrentMonth();
        if (month != currentMonth) {
            withdrawalCount = 0;
            currentMonth = month;
        }
        
        // Check withdrawal limit for Savings accounts
        if (withdrawalCount >= MAX_WITHDRAWALS_PER_MONTH) {
            throw new IllegalStateException("Monthly withdrawal limit exceeded. Maximum " + 
                                          MAX_WITHDRAWALS_PER_MONTH + " withdrawals per month.");
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
        withdrawalCount++;
    }
}