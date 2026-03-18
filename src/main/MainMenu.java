package main;

import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 4;
	private static final int MAX_SELECTION = 4;


	private ArrayList<BankAccount> accounts;
    private Scanner keyboardInput;

    public MainMenu() {
        this.accounts = new ArrayList<>();
        // Start with one account by default
        this.accounts.add(new BankAccount());
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("What would you like to do?");
        System.out.println("1. Make a deposit");
        System.out.println("2. Make a withdrawal");
        System.out.println("3. Check balance");
        System.out.println("4. Exit the app");

    }

    public int getUserSelection(int max) {
        int selection = -1;
        while(selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
        }
        return selection;
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1:
                performDeposit();
                break;
            case 2:
                performWithdraw();
                break;
            case 3:
                performCheckBalance();
                selectAccountAndDeposit();
                break;
            case 2:
                createNewAccount();
                break;
        }
    }

    public void selectAccountAndDeposit() {
        System.out.println("Select account:");
        for(int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". Account " + (i + 1) + " (Balance: $" + accounts.get(i).getBalance() + ")");
        }

        int accountSelection = -1;
        while(accountSelection < 1 || accountSelection > accounts.size()) {
            System.out.print("Enter account number: ");
            accountSelection = keyboardInput.nextInt();
        }

        double depositAmount = -1;
        while(depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextDouble();
        }

        accounts.get(accountSelection - 1).deposit(depositAmount);
        System.out.println("Deposit successful!");
    }

    public void createNewAccount() {
        accounts.add(new BankAccount());
        System.out.println("New account created! You now have " + accounts.size() + " accounts.");
    }

    public void performCheckBalance() {
        System.out.println("Your current balance is: " + userAccount.getBalance());
    }

    public void performWithdraw() {
        System.out.println("Current balance: " + userAccount.getBalance());
        double withdrawAmount = -1;
        while (withdrawAmount <= 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextInt();
        }
        try {
            userAccount.withdraw(withdrawAmount);
            System.out.println("Withdrawal successful. New balance: " + userAccount.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Insufficient funds. Your balance is: " + userAccount.getBalance());
        }
    }

    public void run() {
        int selection = -1;
        while(selection != EXIT_SELECTION) {
            displayOptions();
            selection = getUserSelection(MAX_SELECTION);
            processInput(selection);
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}