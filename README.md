# project26

**Project Description:** A command-line banking application that allows users to create accounts, deposit and withdraw funds, transfer money between accounts, view transaction history, and close accounts.

## Team Members:
* Louis Quartararo
* Demi Babalola
* Ben Kras
* Dalen Ainsworth

## User stories
1. A bank customer should be able to deposit into an existing account. (Shook)
2. A bank customer should be able to withdraw from an account. (Ben)
3. A bank customer should be able to check their account balance. (Ben)
4. A bank customer should be able to view their transaction history for an account. (Dalen)
5. A bank customer should be able to create an additional account with the bank. (Dalen)
6. A bank customer should be able to close an existing account. (Louis)
7. A bank customer should be able to transfer money from one account to another. (Louis)
8. A bank administrator should be able to collect fees from existing accounts when necessary. (Demi)
9. A bank administrator should be able to add an interest payment to an existing account when necessary. (Demi)
10. A bank customer should be able to choose between opening a "Checking" account or a "Savings" account. (Ben)
11. A bank customer with a Checking account should be able to overdraft their account up to a certain limit (e.g., -$100), incurring an automatic overdraft fee. (Ben)
12. A bank customer with a Savings account should be blocked from making more than 6 withdrawals per month. (Dalen)
13. A bank administrator should be able to save the current state of all accounts and transactions to a text or CSV file before exiting the application. (Dalen)
14. A user should be able to protect an account using a PIN. (Demi)
15. A user should be able to take out a loan, so long as it is for an amount less than or equal to their current balance. (Demi)
16. A bank customer should be able to assign a custom nickname to their accounts for easier identification. (Louis)
17. A bank customer should be able to generate a formatted summary of their account showing account type, balance, and transaction totals. (Louis)

## What user stories were completed this iteration?
We completed User Stories 8 through 17. This iteration added administrator features (fees, interest), account types (Checking/Savings) with overdraft and withdrawal limits, file saving, PIN protection, loans, account nicknames, and account summaries. We also refactored MainMenu into separate handler classes for clean code compliance.

## What user stories do you intend to complete next iteration?

User Story 18: A bank administrator should be able to load previously saved account data from a file when starting the application.
User Story 19: A bank customer should be able to view a combined summary of all their accounts showing total balance across all accounts.
User Story 20: A bank customer with a Savings account should earn interest automatically based on their balance.
User Story 21: A bank customer should be able to search their transaction history by keyword.

## Is there anything that you implemented but doesn't currently work?

## What commands are needed to compile and run your code from the command line?
To compile and run the application, ensure the included bash script is executable and run it:
```bash
chmod +x runApp.sh
./runApp.sh
