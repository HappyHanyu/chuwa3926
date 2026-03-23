package com.example.training_project.hw3.Q13;

public class Wallet {
    private double balance;

    public Wallet(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }
        balance += amount;
        System.out.println("Deposited $" + amount + ". New balance: $" + balance);
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Tried to withdraw $" + amount
                            + " but only $" + balance + " available.");
        }
        balance -= amount;
        System.out.println("Withdrew $" + amount + ". New balance: $" + balance);
    }

    public double getBalance() {
        return balance;
    }

    public static void main(String[] args) {
        Wallet wallet = new Wallet(100);

        wallet.deposit(50);

        try {
            wallet.withdraw(200);
        } catch (InsufficientBalanceException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Final balance: $" + wallet.getBalance());
    }
}
