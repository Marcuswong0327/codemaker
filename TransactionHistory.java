import java.io.*; // For ObjectOutputStream, FileOutputStream, IOException, etc.
import java.util.ArrayList; // For ArrayList
import java.util.List; // For List
import Transaction.Transaction; // Ensure this is the correct package path for the Transaction class

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

    public void viewTransactionHistory() {
        System.out.println("\n== Transaction History ==");
        System.out.printf("%-15s %-20s %-10s %-10s %-10s\n", "Date", "Description", "Debit", "Credit", "Balance");

        if (transactions.isEmpty()) {
            System.out.println("No transaction history available.");
        } else {
            for (Transaction t : transactions) {
                System.out.printf("%-15s %-20s %-10.2f %-10.2f %-10.2f\n",
                        t.getDate(), t.getDescription(), t.getDebit(), t.getCredit(), t.getBalance());
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
            for (Transaction t : transactions) {
                writer.printf("%s,%s,%.2f,%.2f,%.2f\n",
                        t.getDate(), t.getDescription(), t.getDebit(), t.getCredit(), t.getBalance());
            }
            System.out.println("Transaction history exported to 'transaction_history.csv'.");
        } catch (FileNotFoundException e) {
            System.out.println("Error exporting transaction history: " + e.getMessage());
        }
    }
}



