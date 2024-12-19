package src.Savings.savings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SavingsCSV {

    private static final String filePath = "savings.csv";

    private List<SavingsRecord> savingsRecords;

    public SavingsCSV() {
        savingsRecords = new ArrayList<>();
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

