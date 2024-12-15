package Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TransactionCSV {

    private static final String filePath = "C:\\Users\\tzeha\\Desktop\\LedgerSystem\\data\\transaction.csv";

    public static void exportTransactions(List<Transaction> transactions) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write the header
            writer.write("Date,Description,Debit,Credit,Balance\n");

            // Write each transaction
            for (Transaction t : transactions) {
                String record = String.format("%s,%s,%s,%s,%.2f\n",
                        t.getDate(),
                        t.getDescription(),
                        t.getDebit() == 0 ? "" : t.getDebit(),
                        t.getCredit() == 0 ? "" : t.getCredit(),
                        t.getBalance());
                writer.write(record);
            }

            System.out.println("File Exported!");
        } catch (IOException e) {
            System.out.println("Error exporting transactions: " + e.getMessage());
        }
    }
}

