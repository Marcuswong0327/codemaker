package src.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TransactionCSV {

    private static final String filePath = "C:\\Users\\tzeha\\Desktop\\LedgerSystem\\data\\transaction.csv";

    public static void exportTransactions(List<Transaction> transactions) {
        double runningBalance = 0.0;  // Track balance dynamically as we write transactions

        try (FileWriter writer = new FileWriter(filePath)) {
            // Write the header
            writer.write("Date,Description,Debit,Credit,Balance\n");

            // Write each transaction
            for (Transaction t : transactions) {
                // Debugging log to check values
                System.out.println("Processing Transaction: " + t.getDescription() + " | Amount: " + t.getAmount());

                // Update the running balance based on the transaction type
                if (t.getType() == Transaction.TransactionType.DEBIT) {
                    runningBalance += t.getAmount();  
                } else if (t.getType() == Transaction.TransactionType.CREDIT) {
                    runningBalance -= t.getAmount();  
                }

                // Prepare debit and credit columns
                String debit = t.getType() == Transaction.TransactionType.DEBIT ? String.format("%.2f", t.getAmount()) : "";
                String credit = t.getType() == Transaction.TransactionType.CREDIT ? String.format("%.2f", t.getAmount()) : "";

                // Debugging log for balance after update
                System.out.println("Updated Balance: " + runningBalance);

                // Write the transaction record
                String record = String.format("%s,%s,%s,%s,%.2f\n",
                        t.getDate(),
                        t.getDescription(),
                        debit, // Debit column
                        credit, // Credit column
                        runningBalance); // Balance after this transaction
                writer.write(record);
            }

            System.out.println("File Exported!");
        } catch (IOException e) {
            System.out.println("Error exporting transactions: " + e.getMessage());
        }
    }
}


