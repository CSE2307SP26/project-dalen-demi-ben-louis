#!/bin/bash

mkdir -p out
javac -d out src/main/*.java
java -cp out main.MainMenu