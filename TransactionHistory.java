import Transaction.Transaction;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class TransactionHistory {

    private List<Transaction> transactions;

    public TransactionHistory() {
        this.transactions = new ArrayList<>();
        loadTransactionHistory(); // Load previous transactions from file
    }

    // Add a new transaction to the list
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction); // Adds the transaction to the list
    }

    // View transaction history with current balance
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

        exportHistoryToCSV(); // Export the history to CSV
    }

    // Save transaction history to a file
    public void saveTransactionHistory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("transactions.dat"))) {
            oos.writeObject(transactions);
        } catch (IOException e) {
            System.out.println("Error saving transaction history: " + e.getMessage());
        }
    }

    // Load previous transaction history from a file
    @SuppressWarnings("unchecked")
    public void loadTransactionHistory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("transactions.dat"))) {
            transactions = (List<Transaction>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous transaction history found.");
            transactions = new ArrayList<>(); // Initialize as an empty list
        }
    }

    // Export history to a CSV file
    private void exportHistoryToCSV() {
        try (PrintWriter writer = new PrintWriter(new File("transaction_history.csv"))) {
            writer.println("Date,Description,Debit,Credit,Balance");
            double currentBalance = 0.0;  // Start with initial balance (zero or any starting value)
            for (Transaction t : transactions) {
                // Update the current balance as we process each transaction
                if (t.getType() == Transaction.TransactionType.DEBIT) {
                    currentBalance -= t.getAmount();
                } else if (t.getType() == Transaction.TransactionType.CREDIT) {
                    currentBalance += t.getAmount();
                }

                // Write each transaction with the updated balance
                writer.printf("%s,%s,%.2f,%.2f,%.2f\n",
                        t.getDate(), t.getDescription(),
                        t.getType() == Transaction.TransactionType.DEBIT ? t.getAmount() : 0.0,
                        t.getType() == Transaction.TransactionType.CREDIT ? t.getAmount() : 0.0,
                        currentBalance);
            }
            System.out.println("Transaction history exported to 'transaction_history.csv'.");
        } catch (FileNotFoundException e) {
            System.out.println("Error exporting transaction history: " + e.getMessage());
        }
    }
}





