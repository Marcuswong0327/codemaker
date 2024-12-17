import java.util.Scanner;
import Transaction.Credit;
import Transaction.Debit;
import Transaction.Transaction;

public class Main {
    public static void main(String[] args) {
        boolean loop = true;
        Scanner k = new Scanner(System.in);
        String key = "";

        // Create an instance of TransactionHistory to manage transactions
        TransactionHistory transactionHistory = new TransactionHistory(1000.00);  // Initialize with a balance (e.g., 1000)
        
        // Create an instance of SavingsSettings to handle savings features
        SavingsSettings savingsSettings = new SavingsSettings(transactionHistory);  // Link with TransactionHistory

        do {
            System.out.println("\n== Transaction Menu ==");
            System.out.println("1. Debit");
            System.out.println("2. Credit");
            System.out.println("3. View Transaction History");
            System.out.println("4. Savings");
            System.out.println("5. Credit Loan");
            System.out.println("6. Deposit Interest Predictor");
            System.out.println("7. Logout");

            System.out.print("\n> ");
            key = k.next();

            switch (key) {
                case "1":
                    System.out.println("\n== Debit ==");
                    Debit debit = new Debit();
                    Transaction debitTransaction = debit.recordDebit(); // Record a debit transaction
                    if (debitTransaction != null) {
                        transactionHistory.addTransaction(debitTransaction);
                        System.out.println("Debit successfully recorded!");
                    } else {
                        System.out.println("Debit transaction failed.");
                    }
                    break;

                case "2":
                    System.out.println("\n== Credit ==");
                    Credit credit = new Credit();
                    Transaction creditTransaction = credit.recordCredit(); // Record a credit transaction
                    if (creditTransaction != null) {
                        transactionHistory.addTransaction(creditTransaction);
                        System.out.println("Credit successfully recorded!");
                    } else {
                        System.out.println("Credit transaction failed.");
                    }
                    break;

                case "3":
                    System.out.println("\n== History ==");
                    transactionHistory.viewTransactionHistory();
                    break;

                case "4":
                    System.out.println("\n== Savings ==");
                    // Handle savings settings (activation, saving percentage, etc.)
                    savingsSettings.activateSavingsFlow();
                    break;

                case "5":
                    System.out.println("\n== Credit Loan ==");
                    System.out.println("Credit loan feature under development.");
                    break;

                case "6":
                    System.out.println("\n== Deposit Interest Predictor ==");
                    System.out.println("Interest predictor feature under development.");
                    break;

                case "7":
                    System.out.println("\nThank you for using \"Ledger System AlgoNauts\".");
                    loop = false;
                    break;

                default:
                    System.out.println("\nError. Invalid selection, please choose again.");
                    break;
            }

            // Save the transaction history after every operation
            transactionHistory.saveTransactionHistory();

        } while (loop);

        // Close the scanner
        k.close();
    }
}




