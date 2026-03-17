package main;

import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 3;
	private static final int MAX_SELECTION = 3;

	private ArrayList<BankAccount> accounts;
    private Scanner keyboardInput;

    public MainMenu() {
        this.accounts = new ArrayList<>();
        // Start with one account by default
        this.accounts.add(new BankAccount());
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("\nWelcome to the 237 Bank App!");
        System.out.println("1. Make a deposit");
        System.out.println("2. Create a new account");
        System.out.println("3. Exit the app");
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