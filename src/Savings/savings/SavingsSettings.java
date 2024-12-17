package src.Savings.savings;

import java.util.Scanner;
import src.TransactionHistory;

public class SavingsSettings {

    private boolean isSavingsActive = false;  // Tracks if savings feature is active
    private int savingsPercentage = 0;       // Default savings percentage
    private double totalSavings = 0.0;       // Accumulated savings amount
    private TransactionHistory transactionHistory; // Links to TransactionHistory for balance updates

    // Constructor that links SavingsSettings with TransactionHistory
    public SavingsSettings(TransactionHistory transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    // Main savings management flow
    public void activateSavingsFlow() {
        Scanner scanner = new Scanner(System.in);

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

        while (isSavingsActive) {
            System.out.println("\nCurrent Balance: " + transactionHistory.getCurrentBalance());
            System.out.print("Enter debit amount (or -1 to simulate end of month): ");
            double debitAmount = scanner.nextDouble();

            if (debitAmount == -1) {
                autoTransferSavingsToBalance();
                break;
            }

            processDebit(debitAmount);
        }
    }

    // Helper method to get valid percentage input
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

    // Activate savings with a specified percentage
    public void activateSavings(int percentage) {
        if (percentage < 0 || percentage > 100) {
            System.out.println("Invalid percentage. Please enter a value between 0 and 100.");
            return;
        }
        isSavingsActive = true;
        savingsPercentage = percentage;
        System.out.println("Savings activated successfully at " + percentage + "%!");
    }

    // Process a debit transaction with savings applied
    public void processDebit(double debitAmount) {
        if (!isSavingsActive) {
            System.out.println("Savings not active. Processing debit without savings.");
            transactionHistory.updateBalance(transactionHistory.getCurrentBalance() - debitAmount);
            return;
        }

        double savingsFromDebit = (debitAmount * savingsPercentage) / 100;
        double remainingDebit = debitAmount;

        if (remainingDebit > transactionHistory.getCurrentBalance()) {
            System.out.println("Insufficient balance for this transaction.");
            return;
        }

        // Deduct the savings and add to totalSavings
        totalSavings += savingsFromDebit;
        remainingDebit -= savingsFromDebit;

        // Update transaction history with adjusted debit
        transactionHistory.updateBalance(transactionHistory.getCurrentBalance() - remainingDebit);

        System.out.printf("Saved: %.2f%n", savingsFromDebit);
        System.out.printf("Remaining Balance: %.2f%n", transactionHistory.getCurrentBalance());
    }

    // Transfer accumulated savings to the main balance at the end of the month
    public void autoTransferSavingsToBalance() {
        System.out.println("\nEnd of the month. Transferring savings to main balance...");
        transactionHistory.updateBalance(transactionHistory.getCurrentBalance() + totalSavings);
        System.out.printf("Total Savings Transferred: %.2f%n", totalSavings);
        System.out.printf("New Balance: %.2f%n", transactionHistory.getCurrentBalance());
        totalSavings = 0.0; // Reset savings for the next month
    }

    // Deactivate savings feature
    public void deactivateSavings() {
        if (isSavingsActive) {
            isSavingsActive = false;
            savingsPercentage = 0;
            System.out.println("Savings feature deactivated.");
        } else {
            System.out.println("Savings feature is not active.");
        }
    }

    // Getter for savings percentage
    public int getSavingsPercentage() {
        return savingsPercentage;
    }

    // Getter for total savings (for testing purposes)
    public double getTotalSavings() {
        return totalSavings;
    }

    // Getter for savings balance to be used in Main.java
    public double getSavingsBalance() {
        return totalSavings;
    }
}









