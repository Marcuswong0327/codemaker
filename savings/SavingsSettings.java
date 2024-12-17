import java.util.Scanner;

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

        // Activate savings
        System.out.print("Do you want to activate savings? (Y/N): ");
        String choice = scanner.nextLine().trim().toUpperCase();

        if (choice.equals("Y")) {
            System.out.print("Enter default savings percentage (0-100): ");
            int percentage = scanner.nextInt();
            activateSavings(percentage);
        } else {
            System.out.println("Savings not activated.");
            return;
        }

        // Process debit transactions or end-of-month transfers
        while (true) {
            System.out.println("\nCurrent Balance: " + transactionHistory.getCurrentBalance());
            System.out.print("Enter debit amount (or -1 to simulate end of month): ");
            double debitAmount = scanner.nextDouble();

            if (debitAmount == -1) {
                autoTransferSavingsToBalance();
                break;
            }

            // Adjust savings percentage per transaction
            System.out.println("Current savings percentage: " + savingsPercentage + "%");
            System.out.print("Change savings percentage for this transaction? (Y/N): ");
            String changeChoice = scanner.next().trim().toUpperCase();

            if (changeChoice.equals("Y")) {
                System.out.print("Enter new savings percentage: ");
                int newPercentage = scanner.nextInt();
                savingsPercentage = newPercentage;
            }

            processDebit(debitAmount);
        }
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
        double remainingDebit = debitAmount - savingsFromDebit;

        if (remainingDebit > transactionHistory.getCurrentBalance()) {
            System.out.println("Insufficient balance for this transaction.");
            return;
        }

        totalSavings += savingsFromDebit;
        transactionHistory.updateBalance(transactionHistory.getCurrentBalance() - remainingDebit);

        System.out.println("Transaction processed.");
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
}






