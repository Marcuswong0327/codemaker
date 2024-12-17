import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Transaction.Transaction;

import java.time.LocalDate;

public class TransactionHistory {

    private List<Transaction> transactions; // List to store transactions
    private double initialBalance;          // Initial balance of the account
    private double currentBalance;          // Current balance of the account

    // Constructor to initialize transaction history with an initial balance
    public TransactionHistory(double initialBalance) {
        this.transactions = new ArrayList<>();
        this.initialBalance = initialBalance;
        this.currentBalance = initialBalance; // Set the starting balance
        loadTransactionHistory();             // Attempt to load existing transaction history
    }

    // Add a new transaction and update the current balance
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);

        // Update balance based on transaction type
        if (transaction.getType() == Transaction.TransactionType.DEBIT) {
            currentBalance -= transaction.getAmount();
        } else if (transaction.getType() == Transaction.TransactionType.CREDIT) {
            currentBalance += transaction.getAmount();
        }

        // Save updated transaction list to file
        saveTransactionHistory();
    }

    // View the complete transaction history with running balance
    public void viewTransactionHistory() {
        System.out.println("\n== History ==");
        System.out.printf("%-15s %-20s %-10s %-10s %-10s\n", "Date", "Description", "Debit", "Credit", "Balance");

        if (transactions.isEmpty()) {
            System.out.println("No transaction history available.");
        } else {
            double currentBalance = 0.0;  // Variable to hold current balance
            for (Transaction t : transactions) {
                // Update the current balance based on transaction type
                if (t.getType() == Transaction.TransactionType.DEBIT) {
                    currentBalance += t.getAmount();
                } else if (t.getType() == Transaction.TransactionType.CREDIT) {
                    currentBalance -= t.getAmount();
                }
                // Print each transaction with the updated balance
                System.out.printf("%-15s %-20s %-10.2f %-10.2f %-10.2f\n",
                        t.getDate(), t.getDescription(),
                        t.getType() == Transaction.TransactionType.DEBIT ? t.getAmount() : 0.0,
                        t.getType() == Transaction.TransactionType.CREDIT ? t.getAmount() : 0.0,
                        currentBalance);  // Use the running balance
            }
        }

        exportHistoryToCSV(); // Export the history to a CSV file
    }

    // Save transaction history to a binary file
    public void saveTransactionHistory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("transactions.dat"))) {
            oos.writeObject(transactions); // Serialize transactions to file
        } catch (IOException e) {
            System.err.println("Error saving transaction history: " + e.getMessage());
        }
    }

    // Load transaction history from a binary file
    @SuppressWarnings("unchecked")
    public void loadTransactionHistory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("transactions.dat"))) {
            transactions = (List<Transaction>) ois.readObject(); // Deserialize transactions
            recalculateBalance(); // Recalculate balance after loading transactions
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous transaction history found. Starting fresh.");
            transactions = new ArrayList<>();
        }
    }

    // Export transaction history to a CSV file
    private void exportHistoryToCSV() {
        try (PrintWriter writer = new PrintWriter(new File("transaction_history.csv"))) {
            writer.println("Date,Description,Debit,Credit,Balance");
            double runningBalance = initialBalance; // Start with the initial balance

            for (Transaction t : transactions) {
                // Update the running balance
                if (t.getType() == Transaction.TransactionType.DEBIT) {
                    runningBalance -= t.getAmount();
                } else if (t.getType() == Transaction.TransactionType.CREDIT) {
                    runningBalance += t.getAmount();
                }

                // Write transaction details to the CSV file
                writer.printf("%s,%s,%.2f,%.2f,%.2f\n",
                        t.getDate(), t.getDescription(),
                        t.getType() == Transaction.TransactionType.DEBIT ? t.getAmount() : 0.0,
                        t.getType() == Transaction.TransactionType.CREDIT ? t.getAmount() : 0.0,
                        runningBalance);
            }

            System.out.println("Transaction history exported to 'transaction_history.csv'.");
        } catch (FileNotFoundException e) {
            System.err.println("Error exporting transaction history: " + e.getMessage());
        }
    }

    // Recalculate the current balance based on transactions
    private void recalculateBalance() {
        currentBalance = initialBalance; // Reset to the initial balance
        for (Transaction t : transactions) {
            if (t.getType() == Transaction.TransactionType.DEBIT) {
                currentBalance -= t.getAmount();
            } else if (t.getType() == Transaction.TransactionType.CREDIT) {
                currentBalance += t.getAmount();
            }
        }
    }

    // Get the current balance
    public double getCurrentBalance() {
        return currentBalance;
    }

    // Update the current balance (used by SavingsSettings)
    public void updateBalance(double newBalance) {
        this.currentBalance = newBalance;
        System.out.printf("Updated Current Balance: %.2f%n", currentBalance);
    }

    // Update the initial balance (e.g., on account reset)
    public void setInitialBalance(double newInitialBalance) {
        this.initialBalance = newInitialBalance;
        recalculateBalance(); // Recalculate balance based on new initial balance
    }
}






