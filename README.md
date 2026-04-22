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
18. A bank administrator should be able to load previously saved account data from a file when starting the application. (Ben)
19. A bank customer should be able to view a combined summary of all their accounts showing total balance across all accounts. (Ben)
20. A bank customer with a Savings account should earn interest automatically based on their balance. (Dalen)
21. A bank customer should be able to search their transaction history by keyword. (Dalen)
22. A bank customer should be able to set a daily withdrawal limit on their account for self-imposed spending control. (Louis)
23. A bank customer should be able to view a mini-statement showing only the last N transactions for an account. (Louis)
24. A user should be able to set scheduled or recurring payments from their checking account. (Demi)
25. A bank administrator should be able to add or remove interest from any account. (Demi)

## What user stories were completed this iteration?
We completed User Stories 18 through 25. This iteration added administrator data loading at startup, a combined-balance summary across all accounts, automatic Savings-account interest accrual, keyword-based transaction search, self-imposed daily withdrawal limits, mini-statements showing the last N transactions, scheduled/recurring payments from Checking accounts, and a dedicated administrator flow for applying or reversing interest on any account. We also reused and extended the existing `AccountHandler` / `AccountSettingsHandler` / `InputHelper` structure from the previous refactor to keep `MainMenu` thin and clean-code compliant.

## Is there anything that you implemented but doesn't currently work?
No. All 176 unit tests pass and every menu option has been exercised end-to-end.

## What commands are needed to compile and run your code from the command line?
To compile and run the application, ensure the included bash script is executable and run it:
```bash
chmod +x runApp.sh
./runApp.sh
```
