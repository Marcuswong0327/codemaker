package src.Savings.savings;

import java.util.ArrayList;
import java.util.List;

public class accountbalance {

    private double balance;  // Current account balance
    private List<Transaction> transactions;  // List to store all transactions

    public accountbalance(double initialBalance) {
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    // Record a debit transaction
    public void recordDebit(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient balance for this debit transaction.");
            return;
        }
        balance -= amount;
        transactions.add(new Transaction("Debit", amount));
        System.out.println("Debit recorded: " + amount);
        displayCurrentBalance();
    }

    // Record a credit transaction
    public void recordCredit(double amount) {
        balance += amount;
        transactions.add(new Transaction("Credit", amount));
        System.out.println("Credit recorded: " + amount);
        displayCurrentBalance();
    }

    // Display the current balance
    public void displayCurrentBalance() {
        System.out.println("\n== Account Balance ==");
        System.out.println("Current Balance: " + balance);
    }

    // Display all transactions for the month
    public void displayMonthlyTransactions() {
        System.out.println("\n== Monthly Transactions ==");
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
        displayCurrentBalance();
    }

    public static void main(String[] args) {
        accountbalance account = new accountbalance(1000);  // Initialize with a balance of 1000
        account.recordCredit(200);  // Example credit transaction
        account.recordDebit(150);   // Example debit transaction
        account.displayMonthlyTransactions();  // Display all transactions
    }

    // Inner class to represent a transaction
    static class Transaction {
        private String type;  // "Debit" or "Credit"
        private double amount;

        public Transaction(String type, double amount) {
            this.type = type;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return type + ": " + amount;
        }
    }
}


