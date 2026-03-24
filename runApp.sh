#!/bin/bash

mkdir -p out
javac -d out src/main/BankAccount.java src/main/MainMenu.java
java -cp out main.MainMenu