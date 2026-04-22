package main;

import java.util.ArrayList;
import java.util.List;

public class CheckingAccount extends BankAccount {

    private static final double OVERDRAFT_LIMIT = 100.0;
    private static final double OVERDRAFT_FEE = 35.0;
    private List<ScheduledPayment> scheduledPayments;

    public CheckingAccount() {
        super();
        this.scheduledPayments = new ArrayList<>();
    }

    private static class ScheduledPayment {
        private String payee;
        private double amount;
        private int frequencyDays;
        private int daysUntilNext;

        private ScheduledPayment(String payee, double amount, int frequencyDays) {
            this.payee = payee;
            this.amount = amount;
            this.frequencyDays = frequencyDays;
            this.daysUntilNext = frequencyDays;
        }
    }

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

    public void addScheduledPayment(String payee, double amount, int frequencyDays) {
        if (isClosed()) {
            throw new IllegalStateException("Account is closed.");
        }
        if (payee == null || payee.trim().isEmpty()) {
            throw new IllegalArgumentException("Payee cannot be empty.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (frequencyDays <= 0) {
            throw new IllegalArgumentException("Frequency must be at least 1 day.");
        }

        String normalizedPayee = payee.trim();
        scheduledPayments.add(new ScheduledPayment(normalizedPayee, amount, frequencyDays));
        transactions.add("Scheduled Payment Set: " + normalizedPayee + " -$"
                + String.format("%.2f", amount) + " every " + frequencyDays + " day(s)");
    }

    public List<String> getScheduledPaymentsSummary() {
        List<String> summary = new ArrayList<>();
        for (int i = 0; i < scheduledPayments.size(); i++) {
            ScheduledPayment payment = scheduledPayments.get(i);
            summary.add((i + 1) + ". " + payment.payee + " -$" + String.format("%.2f", payment.amount)
                    + " every " + payment.frequencyDays + " day(s), due in "
                    + payment.daysUntilNext + " day(s)");
        }
        return summary;
    }

    public int processScheduledPayments(int daysElapsed) {
        if (isClosed()) {
            throw new IllegalStateException("Account is closed.");
        }
        if (daysElapsed <= 0) {
            throw new IllegalArgumentException("Days elapsed must be greater than zero.");
        }

        int processedCount = 0;
        for (ScheduledPayment payment : scheduledPayments) {
            payment.daysUntilNext -= daysElapsed;
            while (payment.daysUntilNext <= 0) {
                if (payment.amount > this.balance + OVERDRAFT_LIMIT) {
                    transactions.add("Scheduled Payment Failed: " + payment.payee + " -$"
                            + String.format("%.2f", payment.amount) + " (insufficient funds)");
                    payment.daysUntilNext += payment.frequencyDays;
                    break;
                }

                processSingleScheduledPayment(payment);
                processedCount++;
                payment.daysUntilNext += payment.frequencyDays;
            }
        }

        return processedCount;
    }

    private void processSingleScheduledPayment(ScheduledPayment payment) {
        this.balance -= payment.amount;
        this.transactions.add("Scheduled Payment: " + payment.payee + " -$"
                + String.format("%.2f", payment.amount));
        if (this.balance < 0) {
            this.balance -= OVERDRAFT_FEE;
            this.transactions.add("Overdraft Fee: -$" + String.format("%.2f", OVERDRAFT_FEE));
        }
    }

}
