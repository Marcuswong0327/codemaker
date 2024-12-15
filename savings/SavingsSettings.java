import java.util.Scanner;

public class SavingsSettings {

    private boolean isSavingsActive = false; // Tracks if savings is activated
    private int savingsPercentage = 0;      // Percentage to save from debit
    private double totalSavings = 0.0;      // Accumulated savings
    private double mainBalance = 1000.0;    // Initial main balance

    public static void main(String[] args) {
        SavingsSettings savingsSettings = new SavingsSettings();
        Scanner scanner = new Scanner(System.in);

        // Activate savings
        System.out.println("== Savings ==");
        System.out.print("Are you sure you want to activate it? (Y/N): ");
        String choice = scanner.nextLine().trim().toUpperCase();

        if (choice.equals("Y")) {
            System.out.print("Please enter the percentage you wish to deduct from the next debit: ");
            int percentage = scanner.nextInt();
            savingsSettings.activateSavings(percentage);
        } else {
            System.out.println("Savings not activated.");
            return;
        }

        // Process debits
        while (true) {
            System.out.println("\nCurrent Main Balance: " + savingsSettings.mainBalance);
            System.out.println("Enter debit amount (or -1 to simulate end of month): ");
            double debitAmount = scanner.nextDouble();

            if (debitAmount == -1) {
                savingsSettings.autoTransferSavingsToBalance();
                break;
            }

            savingsSettings.processDebit(debitAmount);
        }

        scanner.close();
    }

    public void activateSavings(int percentage) {
        if (percentage < 0 || percentage > 100) {
            System.out.println("Invalid percentage. Please enter a value between 0 and 100.");
            return;
        }
        this.isSavingsActive = true;
        this.savingsPercentage = percentage;
        System.out.println("Savings Settings added successfully!!!");
    }

    public void processDebit(double debitAmount) {
        if (!isSavingsActive) {
            System.out.println("Savings is not activated. Debit processed without savings.");
            mainBalance -= debitAmount;
            return;
        }

        double savingsFromDebit = (debitAmount * savingsPercentage) / 100;
        double remainingDebit = debitAmount - savingsFromDebit;

        if (remainingDebit > mainBalance) {
            System.out.println("Insufficient balance for this transaction.");
            return;
        }

        totalSavings += savingsFromDebit;
        mainBalance -= remainingDebit;

        System.out.println("Transaction processed.");
        System.out.printf("Saved:%.2f%n " ,savingsFromDebit);
        System.out.printf("Remaining Main Balance: %.2f%n", mainBalance);

    }

    public void autoTransferSavingsToBalance() {
        System.out.println("\nEnd of the month. Transferring savings to main balance...");
        mainBalance += totalSavings;
        System.out.printf("Total Savings Transferred: %.2f%n " , totalSavings);
        System.out.printf("New Main Balance: %.2f%n", mainBalance);
        totalSavings = 0.0;  // Reset savings for the next month
    }
}


