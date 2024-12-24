package src.Savings.savings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SavingsCSV {

    // private static final String filePath = "savings.csv"
    private static final String filePath = System.getProperty("user.home") + "/Desktop/Ledger/savings.csv";

    private List<SavingsRecord> savingsRecords;

    public SavingsCSV() {
        savingsRecords = new ArrayList<>();
        loadSavingsRecords(); // Load previous records when the object is created  (NEW)
    
    }

    public void addSavingsRecord(SavingsRecord record) {
        savingsRecords.add(record);
    }

    public void exportSavings() {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Savings_id,User_id,Status,Percentage\n");

            for (SavingsRecord record : savingsRecords) {
                String recordLine = String.format("%d,%d,%s,%d\n", 
                        record.getSavingsId(), 
                        record.getUserId(),
                        record.getStatus(),
                        record.getPercentage());
                writer.write(recordLine);
            }

            System.out.println("Savings data exported successfully!");

        } catch (IOException e) {
            System.out.println("Error exporting savings: " + e.getMessage());
        }
    }
    // Load previous savings records from the CSV file (NEW)
    public List<SavingsRecord> loadSavingsRecords() {
        List<SavingsRecord> loadedRecords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Skip the header line

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header
                }

                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int savingsId = Integer.parseInt(parts[0]);
                    int userId = Integer.parseInt(parts[1]);
                    String status = parts[2];
                    int percentage = Integer.parseInt(parts[3]);

                    loadedRecords.add(new SavingsRecord(savingsId, userId, status, percentage));
                }
            }

            System.out.println("Previous savings records loaded successfully!");

        } catch (IOException e) {
            System.out.println("Error loading savings records: " + e.getMessage());
        }

        return loadedRecords;
    }
}

class SavingsRecord {
    private int savingsId;
    private int userId;
    private String status;
    private int percentage;

    public SavingsRecord(int savingsId, int userId, String status, int percentage) {
        this.savingsId = savingsId;
        this.userId = userId;
        this.status = status;
        this.percentage = percentage;
    }

    public int getSavingsId() {
        return savingsId;
    }

    public int getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public int getPercentage() {
        return percentage;
    }
}

