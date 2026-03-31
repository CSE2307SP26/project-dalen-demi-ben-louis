package main;


public class SavingsAccount extends BankAccount {
    
    private static final int MAX_WITHDRAWALS_PER_MONTH = 6;
    private int withdrawalCount;
    private int currentMonth;
    
    public SavingsAccount() {
        super();
        this.withdrawalCount = 0;
        this.currentMonth = getCurrentMonth();
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
            return 0; // Reset count for display purposes
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
        // For simplicity, using the month from current time
        // In a real system, you might want to track this more carefully
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
    withdrawalCount++; // Increment the withdrawal count for transfers
}
}