package src.Savings.savings;

import java.util.Scanner;
import src.TransactionHistory;

public class SavingsSettings {

    private boolean isSavingsActive = false;  // Tracks if savings feature is active
    private int savingsPercentage = 0;        // Default savings percentage
    private double totalSavings = 0.0;        // Accumulated savings amount
    private TransactionHistory transactionHistory; // Links to TransactionHistory for balance updates
    private List<SavingsRecord> savingsRecords; //List to store savings records
    
    // Constructor to link TransactionHistory
    public SavingsSettings(TransactionHistory transactionHistory) {
        this.transactionHistory = transactionHistory;
        this.savingsRecords = new ArrayList<>(); // Initialize the list to store savings records.
        loadPreviousSavingsRecords(); // Load previous savings records (NEW)
    }

    // Flow to activate and set up savings
    public void activateSavingsFlow() {
        Scanner scanner = new Scanner(System.in);

        // Prompt to activate savings
        if (!isSavingsActive) {
            System.out.print("Do you want to activate savings? (Y/N): ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("Y")) {
                System.out.print("Enter default savings percentage (0-100): ");
                int percentage = getValidPercentage(scanner);
                activateSavings(percentage);
            } else {
                System.out.println("Savings not activated.");
            }
        } else {
            System.out.println("Savings already activated with " + savingsPercentage + "%.");
        }
    }

    // Helper method to validate percentage input
    private int getValidPercentage(Scanner scanner) {
        int percentage = -1;
        while (percentage < 0 || percentage > 100) {
            try {
                percentage = Integer.parseInt(scanner.nextLine().trim());
                if (percentage < 0 || percentage > 100) {
                    System.out.println("Please enter a valid percentage between 0 and 100.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 0 and 100.");
            }
        }
        return percentage;
    }

    // Activate the savings feature with a valid percentage
    public void activateSavings(int percentage) {
        isSavingsActive = true;
        savingsPercentage = percentage;
        System.out.println("Savings activated successfully at " + percentage + "%!");
        
        //Export to CSV
        SavingsCSV savingsCSV = new SavingsCSV();
        savingsCSV.addSavingsRecord(new SavingsRecord(1, 101, "Active", percentage));  // Example user_id 101
        savingsCSV.exportSavings();
    }

    // Automatically transfer savings to balance at the end of the month
    public void autoTransferSavingsToBalance() {
        System.out.println("\nEnd of the month. Transferring savings to main balance...");
        System.out.printf("Total Savings: %.2f%n", totalSavings);

        if (totalSavings > 0.0) {
            transactionHistory.updateBalance(transactionHistory.getCurrentBalance() + totalSavings);
            System.out.printf("Total Savings Transferred: %.2f%n", totalSavings);
            totalSavings = 0.0;  // Reset savings after transfer
        } else {
            System.out.println("No savings to transfer.");
        }
        System.out.printf("New Balance: %.2f%n", transactionHistory.getCurrentBalance());
    }

    // Getter for savings percentage
    public int getSavingsPercentage() {
        return savingsPercentage;
    }

    // Getter for total savings balance
    public double getSavingsBalance() {
        return totalSavings;
    }

    // Getter for active status
    public boolean isSavingsActive() {
        return isSavingsActive;
    }

    // Load previous savings records (NEW)
    private void loadPreviousSavingsRecords() {
        SavingsCSV savingsCSV = new SavingsCSV();
        this.savingsRecords = savingsCSV.loadSavingsRecords(); // Load previous records from CSV
        if (!savingsRecords.isEmpty()) {
            SavingsRecord latestRecord = savingsRecords.get(savingsRecords.size() - 1);
            this.isSavingsActive = latestRecord.getStatus().equals("Active");
            this.savingsPercentage = latestRecord.getPercentage();
        }
    }
}












