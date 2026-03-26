# project26

**Project Description:** A command-line banking application that allows users to create accounts, deposit and withdraw funds, transfer money between accounts, view transaction history, and close accounts.

## Team Members:
* Louis Quartararo
* Demi Babalola
* Ben Kras
* Dalen Ainsworth

## User stories
1. A bank customer should be able to deposit into an existing account. (Shook)
2. A bank customer should be able to withdraw from an account. 
3. A bank customer should be able to check their account balance. 
4. A bank customer should be able to view their transaction history for an account. 
5. A bank customer should be able to create an additional account with the bank. 
6. A bank customer should be able to close an existing account.
7. A bank customer should be able to transfer money from one account to another. 
8. A bank administrator should be able to collect fees from existing accounts when necessary.
9. A bank administrator should be able to add an interest payment to an existing account when necessary.

## What user stories were completed this iteration?
We successfully completed User Stories 1 through 7. Customers can fully interact with the command-line menu to open/close accounts, deposit, withdraw, check balances, view transaction history, and transfer money between accounts. 

## What user stories do you intend to complete next iteration?
Next iteration, we intend to complete User Stories 8 and 9. This will involve implementing an "Administrator" mode or menu options that allow authorized users to apply interest payments and collect fees across the bank accounts. 

In addition, we will look to complete these additional user stories:

User Story 10: A bank customer should be able to choose between opening a "Checking" account or a "Savings" account.
User Story 11: A bank customer with a Checking account should be able to overdraft their account up to a certain limit (e.g., -$100), incurring an automatic overdraft fee.
User Story 12: A bank customer with a Savings account should be blocked from making more than 6 withdrawals per month.
User Story 13: A bank administrator should be able to save the current state of all accounts and transactions to a text or CSV file before exiting the application.

## Is there anything that you implemented but doesn't currently work?
We implemented the backend logic for collecting fees (`collectFees` and `addFees` methods in `BankAccount.java`), but it currently "doesn't work" for the user because we have not yet connected it to the `MainMenu` user interface. There is no way for an administrator to trigger these methods from the application menu yet.

## What commands are needed to compile and run your code from the command line?
To compile and run the application, ensure the included bash script is executable and run it:
```bash
chmod +x runApp.sh
./runApp.sh
