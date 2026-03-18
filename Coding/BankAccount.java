package com.example.training_project;

public class BankAccount {
    private String accountNumber;
    private double balance;

    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount("ACC001");
        System.out.println(account.deposit(500));   // true
        System.out.println(account.withdraw(200));  // true
        System.out.println(account.getBalance());   // 300.0
        System.out.println(account.withdraw(500));  // false, insufficient balance
    }
}
