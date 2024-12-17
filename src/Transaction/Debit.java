package src.Transaction;

import java.time.LocalDate;
import java.util.Scanner;
import src.Savings.savings.SavingsSettings;  // Import the SavingsSettings class from the savings package

public class Debit {

    private double debit;
    private String description;
    private Scanner scanner = new Scanner(System.in);
    private SavingsSettings savingsSettings;  // Link to SavingsSettings

    // Default constructor
    public Debit() {
        this.debit = 0;
        this.description = "";
        this.savingsSettings = null;  // No savingsSettings object yet
    }

    // Constructor to link the SavingsSettings object
    public Debit(SavingsSettings savingsSettings) {
        this.debit = 0;
        this.description = "";
        this.savingsSettings = savingsSettings;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Method to record a debit transaction
    public Transaction recordDebit() {
        // Check if savingsSettings is initialized
        if (savingsSettings != null && savingsSettings.getSavingsPercentage() > 0) {
            System.out.println("Savings feature is active.");
        } else {
            System.out.println("Savings Settings not initialized or inactive. Proceeding with normal debit.");
        }

        // Input debit amount
        System.out.print("Enter debit amount: ");
        debit = scanner.nextDouble();
        if (debit <= 0) {
            System.out.println("Debit amount must be positive!");
            return null;
        }

        // Input description
        System.out.print("Enter description: ");
        scanner.nextLine(); // Clear buffer
        description = scanner.nextLine();

        double remainingDebit = debit;

        // If savings is active, calculate savings deduction
        if (savingsSettings != null && savingsSettings.getSavingsPercentage() > 0) {
            double savingsDeduction = (debit * savingsSettings.getSavingsPercentage()) / 100;
            remainingDebit = debit - savingsDeduction;

            if (remainingDebit < 0) {
                System.out.println("Insufficient balance for this transaction after savings deduction.");
                return null;
            }

            System.out.printf("Savings Deducted: %.2f%n", savingsDeduction);
        }

        // Record the debit transaction with the adjusted remaining balance
        return new Transaction(LocalDate.now(), description, remainingDebit, Transaction.TransactionType.DEBIT);
    }
}


